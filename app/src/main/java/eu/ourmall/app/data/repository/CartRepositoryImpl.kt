package eu.ourmall.app.data.repository

import eu.ourmall.app.domain.repository.CartRepository
import eu.ourmall.app.data.local.dao.CartDao
import eu.ourmall.app.data.local.dao.PromoDao
import eu.ourmall.app.data.local.entity.ActivePromoEntity
import eu.ourmall.app.data.remote.api.MockProductApi
import eu.ourmall.app.data.remote.dto.ApiResponse
import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.repository.CartIssueType
import eu.ourmall.app.domain.repository.CartValidationIssue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val promoDao: PromoDao,
    private val api: MockProductApi,
) : CartRepository {

    override fun observeCart(): Flow<Cart> =
        combine(cartDao.observeAll(), promoDao.observe()) { items, promo ->
            items.toCart(promo?.code, promo?.discountPercent ?: 0.0)
        }

    override suspend fun addToCart(product: Product, quantity: Int): Result<Unit> {
        return try {
            /** Duplicate merging: if product already in cart, increment quantity **/
            val existing = cartDao.getById(product.id)
            if (existing != null) {
                cartDao.updateQuantity(product.id, existing.quantity + quantity)
            } else {
                cartDao.upsert(product.toCartEntity(quantity))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int): Result<Unit> {
        return try {
            if (quantity <= 0) {
                cartDao.deleteById(productId)
            } else {
                cartDao.updateQuantity(productId, quantity)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromCart(productId: String): Result<Unit> {
        return try {
            cartDao.deleteById(productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun applyPromoCode(code: String): Result<Double> {
        return try {
            when (val r = api.validatePromoCode(code)) {
                is ApiResponse.Success -> {
                    promoDao.set(
                        ActivePromoEntity(
                            code = r.data.code,
                            discountPercent = r.data.discountPercent,
                            description = r.data.description,
                        )
                    )
                    Result.success(r.data.discountPercent)
                }
                is ApiResponse.Error -> Result.failure(Exception(r.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removePromoCode() = promoDao.clear()

    override suspend fun clearCart() {
        cartDao.clearAll()
        promoDao.clear()
    }

    /**
     * Validates each cart item against fresh API data:
     * 1. Checks if stock is still available
     * 2. Detects price changes
     * 3. Detects offer expiry
     * Automatically updates snapshot prices in the local DB.
     */
    override suspend fun validateAndRefreshCart(): Result<List<CartValidationIssue>> {
        return try {
            val cartItems = cartDao.observeAll().first()
            if (cartItems.isEmpty()) return Result.success(emptyList())

            val productIds = cartItems.map { it.productId }
            val issues = mutableListOf<CartValidationIssue>()

            when (val r = api.validateCartItems(productIds)) {
                is ApiResponse.Error -> return Result.failure(Exception(r.message))
                is ApiResponse.Success -> {
                    val now = Instant.now()
                    r.data.forEach { freshDto ->
                        val fresh = freshDto.toDomain()
                        val cartItem = cartItems.find { it.productId == fresh.id } ?: return@forEach

                        /** Stock check **/
                        if (fresh.stockQuantity == 0) {
                            issues.add(CartValidationIssue(
                                productId = fresh.id,
                                productName = fresh.name,
                                issueType = CartIssueType.OUT_OF_STOCK,
                                detail = "${fresh.name} is no longer available",
                            ))
                        } else if (cartItem.quantity > fresh.stockQuantity) {
                            issues.add(CartValidationIssue(
                                productId = fresh.id,
                                productName = fresh.name,
                                issueType = CartIssueType.INSUFFICIENT_STOCK,
                                detail = "Only ${fresh.stockQuantity} left for ${fresh.name}",
                            ))
                            /** Auto-adjust quantity to available stock **/
                            cartDao.updateQuantity(fresh.id, fresh.stockQuantity)
                        }

                        /** Offer expiry check **/
                        val wasOfferActive = cartItem.offerExpiresAtEpoch != null &&
                                cartItem.offerExpiresAtEpoch > (now.epochSecond - 1)
                        val isOfferActive = fresh.isOfferActive(now)

                        if (wasOfferActive && !isOfferActive) {
                            issues.add(CartValidationIssue(
                                productId = fresh.id,
                                productName = fresh.name,
                                issueType = CartIssueType.OFFER_EXPIRED,
                                detail = "Offer for ${fresh.name} has expired. Price updated to ₦${fresh.originalPrice}",
                            ))
                        }

                        /** Price change check **/
                        val newEffectivePrice = fresh.effectivePrice(now)
                        if (newEffectivePrice != cartItem.snapshotPrice) {
                            if (!wasOfferActive || !isOfferActive) { /** not already flagged as expiry **/
                                issues.add(
                                    CartValidationIssue(
                                    productId = fresh.id,
                                    productName = fresh.name,
                                    issueType = CartIssueType.PRICE_CHANGED,
                                    detail = "Price changed from ₦${cartItem.snapshotPrice} to ₦$newEffectivePrice",
                                )
                                )
                            }
                        }

                        /** Always refresh snapshot price & stock **/
                        val discountAmount = if (isOfferActive) fresh.originalPrice - newEffectivePrice else 0.0
                        cartDao.updatePriceAndStock(
                            id = fresh.id,
                            price = newEffectivePrice,
                            discount = discountAmount,
                            stock = fresh.stockQuantity,
                            expiresAt = fresh.offerExpiresAt?.epochSecond,
                        )
                    }
                }
            }
            Result.success(issues)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
