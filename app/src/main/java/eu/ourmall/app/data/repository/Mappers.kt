package eu.ourmall.app.data.repository

import eu.ourmall.app.data.local.entity.CartItemEntity
import eu.ourmall.app.data.local.entity.OrderEntity
import eu.ourmall.app.data.remote.dto.ProductDto
import eu.ourmall.app.domain.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.ourmall.app.util.OrderStatus
import eu.ourmall.app.util.StockStatus
import java.time.Instant

private val gson = Gson()

/** ProductDto -- Domain Product **/

fun ProductDto.toDomain(): Product = Product(
    id = id,
    name = name,
    imageUrl = imageUrl,
    originalPrice = originalPrice,
    discountPercent = discountPercent,
    offerExpiresAt = offerExpiresAtEpoch?.let { Instant.ofEpochSecond(it) },
    vendorId = vendorId,
    vendorName = vendorName,
    category = category,
    stockQuantity = stockQuantity,
    stockStatus = when {
        stockQuantity == 0 -> StockStatus.OUT_OF_STOCK
        stockQuantity <= 5 -> StockStatus.LOW_STOCK
        else -> StockStatus.IN_STOCK
    }
)

/** CartItemEntity - Domain CartItem **/
fun CartItemEntity.toDomain(): CartItem {
    val product = Product(
        id = productId,
        name = productName,
        imageUrl = imageUrl,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        offerExpiresAt = offerExpiresAtEpoch?.let { Instant.ofEpochSecond(it) },
        vendorId = vendorId,
        vendorName = vendorName,
        category = category,
        stockQuantity = stockQuantity,
        stockStatus = when {
            stockQuantity == 0 -> StockStatus.OUT_OF_STOCK
            stockQuantity <= 5 -> StockStatus.LOW_STOCK
            else -> StockStatus.IN_STOCK
        }
    )
    return CartItem(
        product = product,
        quantity = quantity,
        snapshotPrice = snapshotPrice,
        appliedProductDiscount = appliedProductDiscount,
    )
}

/** Domain Product → CartItemEntity **/
fun Product.toCartEntity(quantity: Int): CartItemEntity {
    val now = Instant.now()
    val effectivePrice = effectivePrice(now)
    val discountAmount = if (isOfferActive(now)) originalPrice - effectivePrice else 0.0
    return CartItemEntity(
        productId = id,
        productName = name,
        imageUrl = imageUrl,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        offerExpiresAtEpoch = offerExpiresAt?.epochSecond,
        vendorId = vendorId,
        vendorName = vendorName,
        category = category,
        stockQuantity = stockQuantity,
        quantity = quantity,
        snapshotPrice = effectivePrice,
        appliedProductDiscount = discountAmount,
    )
}

/** List<CartItemEntity> - Cart domain object **/
fun List<CartItemEntity>.toCart(promoCode: String?, promoDiscountPercent: Double): Cart {
    val items = map { it.toDomain() }
    val vendorGroups = items.groupBy { it.product.vendorId }
    val vendorCarts = vendorGroups.map { (vendorId, cartItems) ->
        VendorCart(
            vendorId = vendorId,
            vendorName = cartItems.first().product.vendorName,
            items = cartItems,
        )
    }
    return Cart(
        vendorCarts = vendorCarts,
        cartLevelDiscountPercent = promoDiscountPercent,
        promoCode = promoCode,
    )
}

/** Order - OrderEntity (serialised) **/
fun Order.toEntity(): OrderEntity = OrderEntity(
    id = id,
    createdAtEpoch = createdAt.epochSecond,
    statusJson = status.name,
    vendorOrdersJson = gson.toJson(vendorOrders),
    cartLevelDiscountAmount = cartLevelDiscountAmount,
    promoCode = promoCode,
)

fun OrderEntity.toDomain(): Order {
    val type = object : TypeToken<List<VendorOrder>>() {}.type
    val vendorOrders: List<VendorOrder> = gson.fromJson(vendorOrdersJson, type)
    return Order(
        id = id,
        createdAt = Instant.ofEpochSecond(createdAtEpoch),
        vendorOrders = vendorOrders,
        cartLevelDiscountAmount = cartLevelDiscountAmount,
        promoCode = promoCode,
        status = OrderStatus.valueOf(statusJson),
    )
}
