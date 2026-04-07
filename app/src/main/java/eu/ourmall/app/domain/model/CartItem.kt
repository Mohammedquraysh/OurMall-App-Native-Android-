package eu.ourmall.app.domain.model


data class CartItem(
    val product: Product,
    val quantity: Int,
    val snapshotPrice: Double,  /** price at time of adding to cart **/
    val appliedProductDiscount: Double,   /** discount amount applied to this item **/
) {
    val lineTotal: Double get() = snapshotPrice * quantity
    val discountedLineTotal: Double get() = (snapshotPrice - appliedProductDiscount) * quantity
}
