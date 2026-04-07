package eu.ourmall.app.domain.model

import eu.ourmall.app.util.OrderItemStatus

data class OrderItem(
    val id: String,
    val productId: String,
    val productName: String,
    val imageUrl: String,
    val vendorId: String,
    val vendorName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discountAmount: Double,
    var status: OrderItemStatus,
    val refundAmount: Double = 0.0,
) {
    val lineTotal: Double get() = (unitPrice - discountAmount) * quantity
    val canBeCancelled: Boolean
        get() = status == OrderItemStatus.PENDING || status == OrderItemStatus.CONFIRMED
}
