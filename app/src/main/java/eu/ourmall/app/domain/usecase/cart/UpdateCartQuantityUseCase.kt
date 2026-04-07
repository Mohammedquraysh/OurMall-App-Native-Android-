package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke(productId: String, quantity: Int): Result<Unit> =
        repo.updateQuantity(productId, quantity)
}
