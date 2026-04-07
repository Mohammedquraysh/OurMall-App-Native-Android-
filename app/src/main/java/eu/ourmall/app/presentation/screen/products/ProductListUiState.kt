package eu.ourmall.app.presentation.screen.products

import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.model.ProductFilter

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val categories: List<String> = emptyList(),
    val filter: ProductFilter = ProductFilter(),
    val hasNextPage: Boolean = false,
    val currentPage: Int = 0,
    val cartItemCount: Int = 0,
    val addToCartSuccess: String? = null,  // product name for snackbar
)