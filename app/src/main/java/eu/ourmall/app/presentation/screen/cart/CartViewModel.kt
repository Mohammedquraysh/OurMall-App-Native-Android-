package eu.ourmall.app.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.usecase.cart.ApplyPromoCodeUseCase
import eu.ourmall.app.domain.usecase.cart.ClearCartUseCase
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase
import eu.ourmall.app.domain.usecase.cart.RemoveFromCartUseCase
import eu.ourmall.app.domain.usecase.cart.UpdateCartQuantityUseCase
import eu.ourmall.app.domain.usecase.cart.ValidateCartUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private val observeCart: ObserveCartUseCase,
    private val updateQty: UpdateCartQuantityUseCase,
    private val removeItem: RemoveFromCartUseCase,
    private val applyPromo: ApplyPromoCodeUseCase,
    private val validateCart: ValidateCartUseCase,
    private val clearCart: ClearCartUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCart().onEach { cart ->
            _uiState.update { it.copy(cart = cart) }
        }.launchIn(viewModelScope)

        /** Auto-refresh offer expiry in cart every 30s **/
        viewModelScope.launch {
            while (true) {
                delay(30_000)
                refreshCartPrices()
            }
        }
    }

    fun onQuantityChanged(productId: String, qty: Int) {
        viewModelScope.launch { updateQty(productId, qty) }
    }

    fun onRemoveItem(productId: String) {
        viewModelScope.launch { removeItem(productId) }
    }

    fun onPromoInputChanged(value: String) {
        _uiState.update { it.copy(promoInput = value, promoError = null) }
    }

    fun onApplyPromo() {
        val code = _uiState.value.promoInput.trim()
        if (code.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isApplyingPromo = true, promoError = null) }
            applyPromo(code)
                .onSuccess { pct ->
                    _uiState.update {
                        it.copy(
                            isApplyingPromo = false,
                            promoSuccess = "Code applied! ${pct.toInt()}% off",
                            promoInput = "",
                        )
                    }
                    delay(3000)
                    _uiState.update { it.copy(promoSuccess = null) }
                }
                .onFailure { err ->
                    _uiState.update { it.copy(isApplyingPromo = false, promoError = err.message) }
                }
        }
    }

    fun onProceedToCheckout(onValidated: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isValidating = true, validationIssues = emptyList()) }
            validateCart()
                .onSuccess { issues ->
                    _uiState.update { it.copy(isValidating = false, validationIssues = issues) }
                    if (issues.isEmpty()) onValidated()
                }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(isValidating = false, error = err.message)
                    }
                }
        }
    }

    fun dismissValidationIssues() = _uiState.update { it.copy(validationIssues = emptyList()) }
    fun clearError() = _uiState.update { it.copy(error = null) }

    private suspend fun refreshCartPrices() {
        validateCart() /** silently refresh prices **/
    }
}
