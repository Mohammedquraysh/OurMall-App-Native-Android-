package eu.ourmall.app.domain.model

import eu.ourmall.app.util.OrderItemStatus


data class VendorOrder(
    val vendorId: String,
    val vendorName: String,
    val items: List<OrderItem>,
    val subtotal: Double,
) {
    val activeItems: List<OrderItem> get() = items.filter { it.status != OrderItemStatus.CANCELLED }
    val activeSubtotal: Double get() = activeItems.sumOf { it.lineTotal }
}

