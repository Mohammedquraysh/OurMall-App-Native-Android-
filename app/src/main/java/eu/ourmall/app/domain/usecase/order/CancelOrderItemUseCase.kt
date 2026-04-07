package eu.ourmall.app.domain.usecase.order

import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.repository.OrderRepository
import javax.inject.Inject

class CancelOrderItemUseCase @Inject constructor(private val repo: OrderRepository) {
    suspend operator fun invoke(orderId: String, itemId: String): Result<Order> =
        repo.cancelOrderItem(orderId, itemId)
}
