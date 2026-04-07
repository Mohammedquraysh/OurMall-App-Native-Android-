package eu.ourmall.app.presentation.screen.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.usecase.order.CancelOrderItemUseCase
import eu.ourmall.app.domain.usecase.order.CancelOrderUseCase
import eu.ourmall.app.domain.usecase.order.GetOrderByIdUseCase
import eu.ourmall.app.util.formatPrice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderById: GetOrderByIdUseCase,
    private val cancelOrder: CancelOrderUseCase,
    private val cancelItem: CancelOrderItemUseCase,
) : ViewModel()
{

    private val orderId: String = checkNotNull(savedStateHandle["orderId"])
    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        getOrderById(orderId).onEach { result ->
            result
                .onSuccess { order -> _uiState.update { it.copy(order = order, isLoading = false) } }
                .onFailure { err  -> _uiState.update { it.copy(isLoading = false, error = err.message) } }
        }.launchIn(viewModelScope)
    }

    fun cancelFullOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCancelling = true, error = null) }
            cancelOrder(orderId)
                .onSuccess { updated ->
                    _uiState.update {
                        it.copy(
                            isCancelling = false, order = updated,
                            cancelSuccess = "Order cancelled. Refund: ${updated.totalRefunded.formatPrice()}",
                        )
                    }
                }
                .onFailure { err -> _uiState.update { it.copy(isCancelling = false, error = err.message) } }
        }
    }

    fun cancelSingleItem(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCancelling = true, error = null) }
            cancelItem(orderId, itemId)
                .onSuccess { updated ->
                    val refund = updated.allItems.find { it.id == itemId }?.refundAmount?.formatPrice()
                    _uiState.update {
                        it.copy(
                            isCancelling = false, order = updated,
                            cancelSuccess = "Item cancelled. Refund: ${refund ?: "N/A"}",
                        )
                    }
                }
                .onFailure { err -> _uiState.update { it.copy(isCancelling = false, error = err.message) } }
        }
    }

    fun clearSuccess() = _uiState.update { it.copy(cancelSuccess = null) }
    fun clearError()   = _uiState.update { it.copy(error = null) }
}
