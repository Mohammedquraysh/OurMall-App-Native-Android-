package eu.ourmall.app.domain.model


data class Cart(
    val vendorCarts: List<VendorCart>,
    val cartLevelDiscountPercent: Double = 0.0,  /** e.g. promo code discount **/
    val promoCode: String? = null,
) {
    val subtotalBeforeCartDiscount: Double get() = vendorCarts.sumOf { it.subtotal }
    val cartLevelDiscountAmount: Double get() = subtotalBeforeCartDiscount * (cartLevelDiscountPercent / 100.0)
    val grandTotal: Double get() = subtotalBeforeCartDiscount - cartLevelDiscountAmount
    val totalItems: Int get() = vendorCarts.sumOf { it.itemCount }
    val isEmpty: Boolean get() = vendorCarts.isEmpty() || totalItems == 0
}