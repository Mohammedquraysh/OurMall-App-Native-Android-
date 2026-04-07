package eu.ourmall.app.presentation.screen.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.repository.CartIssueType
import eu.ourmall.app.domain.usecase.cart.ClearCartUseCase
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase
import eu.ourmall.app.domain.usecase.cart.ValidateCartUseCase
import eu.ourmall.app.domain.usecase.order.CreateOrderUseCase
import eu.ourmall.app.util.CheckoutStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val observeCart: ObserveCartUseCase,
    private val validateCart: ValidateCartUseCase,
    private val createOrder: CreateOrderUseCase,
    private val clearCart: ClearCartUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        observeCart().onEach { cart ->
            _uiState.update { it.copy(cart = cart) }
        }.launchIn(viewModelScope)
    }

    fun placeOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isValidating = true, error = null) }

            /** Step 1 : Pre-checkout validation **/
            val validationResult = validateCart()
            val issues = validationResult.getOrElse { err ->
                _uiState.update { it.copy(isValidating = false, error = err.message) }
                return@launch
            }

            _uiState.update { it.copy(isValidating = false, validationIssues = issues) }

            /** Block hard failures (out of stock) **/
            val hasBlockers = issues.any {
                it.issueType == CartIssueType.OUT_OF_STOCK
            }
            if (hasBlockers) return@launch

            /** Step 2: Create order **/
            _uiState.update { it.copy(isPlacingOrder = true, step = CheckoutStep.PLACING) }

            val currentCart = _uiState.value.cart
            createOrder(currentCart)
                .onSuccess { order ->
                    clearCart()
                    _uiState.update {
                        it.copy(
                            isPlacingOrder = false,
                            orderId = order.id,
                            step = CheckoutStep.SUCCESS,
                        )
                    }
                }
                .onFailure { err ->
                    _uiState.update {
                        it.copy(
                            isPlacingOrder = false,
                            error = err.message,
                            step = CheckoutStep.REVIEW,
                        )
                    }
                }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}
