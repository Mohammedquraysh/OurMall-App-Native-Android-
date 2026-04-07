package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.repository.CartRepository
import javax.inject.Inject


class ClearCartUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke() = repo.clearCart()
}
