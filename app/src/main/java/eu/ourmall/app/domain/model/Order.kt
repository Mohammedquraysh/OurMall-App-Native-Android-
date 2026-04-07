package eu.ourmall.app.domain.model

import eu.ourmall.app.util.OrderItemStatus
import eu.ourmall.app.util.OrderStatus
import java.time.Instant


data class Order(
    val id: String,
    val createdAt: Instant,
    val vendorOrders: List<VendorOrder>,
    val cartLevelDiscountAmount: Double,
    val promoCode: String?,
    var status: OrderStatus,
) {
    val activeVendorOrders: List<VendorOrder>
        get() = vendorOrders.filter { vo -> vo.activeItems.isNotEmpty() }

    val grandTotal: Double
        get() = vendorOrders.sumOf { it.activeSubtotal } - cartLevelDiscountAmount

    val totalRefunded: Double
        get() = vendorOrders.flatMap { it.items }
            .filter { it.status == OrderItemStatus.CANCELLED }
            .sumOf { it.refundAmount }

    val allItems: List<OrderItem>
        get() = vendorOrders.flatMap { it.items }
}
