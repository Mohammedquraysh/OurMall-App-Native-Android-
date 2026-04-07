package eu.ourmall.app.domain.repository

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<Cart>
    suspend fun addToCart(product: Product, quantity: Int): Result<Unit>
    suspend fun updateQuantity(productId: String, quantity: Int): Result<Unit>
    suspend fun removeFromCart(productId: String): Result<Unit>
    suspend fun applyPromoCode(code: String): Result<Double> /** returns discount percent **/
    suspend fun removePromoCode()
    suspend fun clearCart()

    /** Refreshes all snapshot prices & validates stock — call before checkout */
    suspend fun validateAndRefreshCart(): Result<List<CartValidationIssue>>
}