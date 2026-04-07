package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke(product: Product, quantity: Int = 1): Result<Unit> =
        repo.addToCart(product, quantity)
}