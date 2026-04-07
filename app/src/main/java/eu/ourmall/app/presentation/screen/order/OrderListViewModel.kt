package eu.ourmall.app.presentation.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ourmall.app.domain.usecase.order.GetOrdersUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class OrderListViewModel @Inject constructor(
    getOrders: GetOrdersUseCase,
) : ViewModel() {

    val uiState: StateFlow<OrderListUiState> =
        getOrders().map { result ->
            result.fold(
                onSuccess = { OrderListUiState(orders = it, isLoading = false) },
                onFailure = { OrderListUiState(isLoading = false, error = it.message) }
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OrderListUiState())
}
