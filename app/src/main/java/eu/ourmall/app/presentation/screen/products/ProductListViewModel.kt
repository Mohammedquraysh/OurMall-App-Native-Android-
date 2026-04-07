package eu.ourmall.app.presentation.screen.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.model.*
import eu.ourmall.app.domain.usecase.cart.AddToCartUseCase
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase
import eu.ourmall.app.domain.usecase.product.*
import eu.ourmall.app.util.StockStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject



@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val addToCart: AddToCartUseCase,
    private val observeCart: ObserveCartUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCategories()
        loadProducts(reset = true)
        observeCartCount()
    }

    private fun observeCartCount() {
        observeCart().onEach { cart ->
            _uiState.update { it.copy(cartItemCount = cart.totalItems) }
        }.launchIn(viewModelScope)
    }

    private fun loadCategories() {
        getCategories().onEach { result ->
            result.onSuccess { cats ->
                _uiState.update { it.copy(categories = cats) }
            }
        }.launchIn(viewModelScope)
    }

    fun loadProducts(reset: Boolean = false) {
        val state = _uiState.value
        val page = if (reset) 0 else state.currentPage + 1
        if (!reset && !state.hasNextPage) return
        if (state.isLoadingMore && !reset) return

        viewModelScope.launch {
            _uiState.update {
                if (reset) it.copy(isLoading = true, error = null, currentPage = 0)
                else it.copy(isLoadingMore = true)
            }

            getProducts(state.filter, page, PAGE_SIZE)
                .collect { result ->
                    result.onSuccess { newProducts ->
                        _uiState.update { s ->
                            val combined = if (reset) newProducts else s.products + newProducts
                            s.copy(
                                products = combined,
                                isLoading = false,
                                isLoadingMore = false,
                                currentPage = page,
                                hasNextPage = newProducts.size >= PAGE_SIZE,
                                error = null,
                            )
                        }
                    }.onFailure { err ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoadingMore = false,
                                error = err.message ?: "Failed to load products",
                            )
                        }
                    }
                }
        }
    }

    fun onSearchQuery(query: String) {
        _uiState.update { it.copy(filter = it.filter.copy(searchQuery = query)) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400) /** debounce **/
            loadProducts(reset = true)
        }
    }

    fun onCategorySelected(category: String?) {
        _uiState.update { it.copy(filter = it.filter.copy(category = category)) }
        loadProducts(reset = true)
    }

    fun onPriceRangeChanged(min: Double?, max: Double?) {
        _uiState.update { it.copy(filter = it.filter.copy(minPrice = min, maxPrice = max)) }
        loadProducts(reset = true)
    }

    fun onStockFilterChanged(status: StockStatus?) {
        _uiState.update { it.copy(filter = it.filter.copy(stockStatus = status)) }
        loadProducts(reset = true)
    }

    fun onAddToCart(product: Product) {
        viewModelScope.launch {
            addToCart(product, 1).onSuccess {
                _uiState.update { it.copy(addToCartSuccess = product.name) }
                delay(2500)
                _uiState.update { it.copy(addToCartSuccess = null) }
            }
        }
    }

    fun loadNextPage() = loadProducts(reset = false)

    fun clearError() = _uiState.update { it.copy(error = null) }

    companion object { const val PAGE_SIZE = 10 }
}
