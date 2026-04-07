package eu.ourmall.app.domain.repository

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(cart: Cart): Result<Order>
    fun getOrders(): Flow<Result<List<Order>>>
    fun getOrderById(id: String): Flow<Result<Order>>
    suspend fun cancelOrder(orderId: String): Result<Order>
    suspend fun cancelOrderItem(orderId: String, itemId: String): Result<Order>
}