package eu.ourmall.app.domain.model

data class VendorCart(
    val vendorId: String,
    val vendorName: String,
    val items: List<CartItem>,
) {
    val subtotal: Double get() = items.sumOf { it.discountedLineTotal }
    val totalDiscount: Double get() = items.sumOf { it.appliedProductDiscount * it.quantity }
    val itemCount: Int get() = items.sumOf { it.quantity }
}
