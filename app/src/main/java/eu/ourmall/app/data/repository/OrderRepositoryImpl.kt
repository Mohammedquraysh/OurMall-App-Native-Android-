package eu.ourmall.app.data.repository

import eu.ourmall.app.data.local.dao.OrderDao
import eu.ourmall.app.domain.model.*
import eu.ourmall.app.domain.repository.OrderRepository
import eu.ourmall.app.util.OrderItemStatus
import eu.ourmall.app.util.OrderStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
) : OrderRepository {

    override suspend fun createOrder(cart: Cart): Result<Order> {
        return try {
            delay(600) /** simulate server round-trip **/
            val orderId = "ORD-${UUID.randomUUID().toString().take(8).uppercase()}"

            /** Build vendor sub-orders — one per vendor **/
            val vendorOrders = cart.vendorCarts.map { vendorCart ->
                val orderItems = vendorCart.items.mapIndexed { index, cartItem ->
                    OrderItem(
                        id = "${orderId}-${vendorCart.vendorId}-$index",
                        productId = cartItem.product.id,
                        productName = cartItem.product.name,
                        imageUrl = cartItem.product.imageUrl,
                        vendorId = cartItem.product.vendorId,
                        vendorName = cartItem.product.vendorName,
                        quantity = cartItem.quantity,
                        unitPrice = cartItem.snapshotPrice,
                        discountAmount = cartItem.appliedProductDiscount,
                        status = OrderItemStatus.PENDING,
                    )
                }
                VendorOrder(
                    vendorId = vendorCart.vendorId,
                    vendorName = vendorCart.vendorName,
                    items = orderItems,
                    subtotal = vendorCart.subtotal,
                )
            }

            val order = Order(
                id = orderId,
                createdAt = Instant.now(),
                vendorOrders = vendorOrders,
                cartLevelDiscountAmount = cart.cartLevelDiscountAmount,
                promoCode = cart.promoCode,
                status = OrderStatus.PENDING,
            )

            orderDao.upsert(order.toEntity())
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getOrders(): Flow<Result<List<Order>>> =
        orderDao.observeAll().map { entities ->
            try {
                Result.success(entities.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun getOrderById(id: String): Flow<Result<Order>> =
        orderDao.observeById(id).map { entity ->
            if (entity != null) {
                try { Result.success(entity.toDomain()) }
                catch (e: Exception) { Result.failure(e) }
            } else {
                Result.failure(Exception("Order $id not found"))
            }
        }

    /** Cancel the entire order — marks all cancellable items as CANCELLED */
    override suspend fun cancelOrder(orderId: String): Result<Order> {
        return try {
            delay(400)
            val entity = orderDao.getById(orderId)
                ?: return Result.failure(Exception("Order not found"))
            val order = entity.toDomain()

            val updatedVendorOrders = order.vendorOrders.map { vo ->
                vo.copy(items = vo.items.map { item ->
                    if (item.canBeCancelled) {
                        item.copy(
                            status = OrderItemStatus.CANCELLED,
                            refundAmount = item.lineTotal,
                        )
                    } else item
                })
            }

            val allCancelled = updatedVendorOrders
                .flatMap { it.items }
                .none { it.status != OrderItemStatus.CANCELLED }

            val newStatus = if (allCancelled) OrderStatus.CANCELLED else OrderStatus.PARTIALLY_CANCELLED

            val updatedOrder = order.copy(
                vendorOrders = updatedVendorOrders,
                status = newStatus,
            )
            orderDao.upsert(updatedOrder.toEntity())
            Result.success(updatedOrder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Cancel a single item — refund only that item, recalculate total */
    override suspend fun cancelOrderItem(orderId: String, itemId: String): Result<Order> {
        return try {
            delay(400)
            val entity = orderDao.getById(orderId)
                ?: return Result.failure(Exception("Order not found"))
            val order = entity.toDomain()

            val targetItem = order.allItems.find { it.id == itemId }
                ?: return Result.failure(Exception("Item $itemId not found in order"))

            if (!targetItem.canBeCancelled) {
                return Result.failure(Exception("Item cannot be cancelled in status: ${targetItem.status}"))
            }

            val updatedVendorOrders = order.vendorOrders.map { vo ->
                vo.copy(items = vo.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(
                            status = OrderItemStatus.CANCELLED,
                            refundAmount = item.lineTotal, /** full line total refunded **/
                        )
                    } else item
                })
            }

            /** Determine new order-level status **/
            val allItems = updatedVendorOrders.flatMap { it.items }
            val newStatus = when {
                allItems.all { it.status == OrderItemStatus.CANCELLED } -> OrderStatus.CANCELLED
                allItems.any { it.status == OrderItemStatus.CANCELLED } -> OrderStatus.PARTIALLY_CANCELLED
                else -> order.status
            }

            val updatedOrder = order.copy(
                vendorOrders = updatedVendorOrders,
                status = newStatus,
            )
            orderDao.upsert(updatedOrder.toEntity())
            Result.success(updatedOrder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
