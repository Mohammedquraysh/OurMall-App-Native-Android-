package eu.ourmall.app.presentation.screen.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.usecase.cart.AddToCartUseCase
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase
import eu.ourmall.app.domain.usecase.product.GetProductByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductById: GetProductByIdUseCase,
    private val addToCart: AddToCartUseCase,
    private val observeCart: ObserveCartUseCase,
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
        observeCart().onEach { cart ->
            _uiState.update { it.copy(cartItemCount = cart.totalItems) }
        }.launchIn(viewModelScope)
    }

    private fun loadProduct() {
        getProductById(productId).onEach { result ->
            result.onSuccess { p ->
                _uiState.update { it.copy(product = p, isLoading = false, error = null) }
            }.onFailure { err ->
                _uiState.update { it.copy(isLoading = false, error = err.message) }
            }
        }.launchIn(viewModelScope)
    }

    fun increaseQty() = _uiState.update {
        it.copy(quantity = (it.quantity + 1).coerceAtMost(it.product?.stockQuantity ?: 1))
    }

    fun decreaseQty() = _uiState.update { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1)) }

    fun addToCart() {
        val product = _uiState.value.product ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            addToCart(product, _uiState.value.quantity).onSuccess {
                _uiState.update { it.copy(addedToCartSuccess = true, isAddingToCart = false) }
                delay(2000)
                _uiState.update { it.copy(addedToCartSuccess = false) }
            }.onFailure {
                _uiState.update { it.copy(isAddingToCart = false) }
            }
        }
    }
}
