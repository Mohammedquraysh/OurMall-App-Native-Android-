package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCartUseCase @Inject constructor(private val repo: CartRepository) {
    operator fun invoke(): Flow<Cart> = repo.observeCart()
}