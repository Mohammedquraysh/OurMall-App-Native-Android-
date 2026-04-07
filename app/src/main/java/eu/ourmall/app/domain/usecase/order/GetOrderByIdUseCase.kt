package eu.ourmall.app.domain.usecase.order

import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(private val repo: OrderRepository) {
    operator fun invoke(id: String): Flow<Result<Order>> = repo.getOrderById(id)
}
