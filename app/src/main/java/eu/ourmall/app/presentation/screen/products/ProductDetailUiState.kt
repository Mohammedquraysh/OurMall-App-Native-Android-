package eu.ourmall.app.presentation.screen.products

import eu.ourmall.app.domain.model.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val quantity: Int = 1,
    val cartItemCount: Int = 0,
    val addedToCartSuccess: Boolean = false,
    val isAddingToCart: Boolean = false,
)
