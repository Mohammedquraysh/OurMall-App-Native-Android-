package eu.ourmall.app.presentation.screen.order

import eu.ourmall.app.domain.model.Order

data class OrderDetailUiState(
    val order: Order? = null,
    val isLoading: Boolean = true,
    val isCancelling: Boolean = false,
    val error: String? = null,
    val cancelSuccess: String? = null,
)
