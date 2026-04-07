package eu.ourmall.app.presentation.screen.cart

import eu.ourmall.app.domain.model.Cart
import eu.ourmall.app.domain.repository.CartValidationIssue

data class CartUiState(
    val cart: Cart = Cart(emptyList()),
    val isLoading: Boolean = false,
    val promoInput: String = "",
    val isApplyingPromo: Boolean = false,
    val promoError: String? = null,
    val promoSuccess: String? = null,
    val validationIssues: List<CartValidationIssue> = emptyList(),
    val isValidating: Boolean = false,
    val error: String? = null,
)