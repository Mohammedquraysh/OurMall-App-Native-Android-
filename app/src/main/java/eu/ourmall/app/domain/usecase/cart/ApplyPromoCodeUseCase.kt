package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.repository.CartRepository
import javax.inject.Inject


class ApplyPromoCodeUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke(code: String): Result<Double> = repo.applyPromoCode(code)
}