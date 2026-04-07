package eu.ourmall.app.domain.usecase.order

import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.repository.OrderRepository
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(private val repo: OrderRepository) {
    suspend operator fun invoke(orderId: String): Result<Order> = repo.cancelOrder(orderId)
}
