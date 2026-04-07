package eu.ourmall.app.domain.usecase.cart

import eu.ourmall.app.domain.repository.CartRepository
import eu.ourmall.app.domain.repository.CartValidationIssue
import javax.inject.Inject

class ValidateCartUseCase @Inject constructor(private val repo: CartRepository) {
    suspend operator fun invoke(): Result<List<CartValidationIssue>> =
        repo.validateAndRefreshCart()
}
