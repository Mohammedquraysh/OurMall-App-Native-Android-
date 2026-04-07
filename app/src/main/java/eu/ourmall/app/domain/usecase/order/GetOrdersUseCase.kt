package eu.ourmall.app.domain.usecase.order

import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(private val repo: OrderRepository) {
    operator fun invoke(): Flow<Result<List<Order>>> = repo.getOrders()
}
