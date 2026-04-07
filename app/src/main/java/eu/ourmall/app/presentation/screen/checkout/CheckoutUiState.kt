package eu.ourmall.app.presentation.screen.checkout

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.repository.CartValidationIssue
import eu.ourmall.app.util.CheckoutStep

data class CheckoutUiState(
    val cart: Cart = Cart(emptyList()),
    val isPlacingOrder: Boolean = false,
    val isValidating: Boolean = false,
    val validationIssues: List<CartValidationIssue> = emptyList(),
    val error: String? = null,
    val orderId: String? = null,
    val step: CheckoutStep = CheckoutStep.REVIEW,
)
