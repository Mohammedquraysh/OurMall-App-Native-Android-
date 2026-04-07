package eu.ourmall.app.presentation.screen.order

import eu.ourmall.app.domain.model.Order

data class OrderListUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)