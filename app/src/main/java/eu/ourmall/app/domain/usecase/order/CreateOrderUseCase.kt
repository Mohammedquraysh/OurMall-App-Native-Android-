package eu.ourmall.app.domain.usecase.order

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.repository.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(private val repo: OrderRepository) {
    suspend operator fun invoke(cart: Cart): Result<Order> = repo.createOrder(cart)
}