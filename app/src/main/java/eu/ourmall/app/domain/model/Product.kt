package eu.ourmall.app.domain.model

import eu.ourmall.app.util.StockStatus
import java.time.Instant

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String,
    val originalPrice: Double,
    val discountPercent: Double, /** 0.0 = no discount **/
    val offerExpiresAt: Instant?, /** null = no limited offer **/
    val vendorId: String,
    val vendorName: String,
    val category: String,
    val stockQuantity: Int,
    val stockStatus: StockStatus,
) {
    /** Effective price — falls back to originalPrice when offer has expired */
    fun effectivePrice(now: Instant = Instant.now()): Double {
        val offerActive = offerExpiresAt != null && now.isBefore(offerExpiresAt)
        return if (offerActive && discountPercent > 0.0) {
            originalPrice * (1.0 - discountPercent / 100.0)
        } else {
            originalPrice
        }
    }

    fun isOfferActive(now: Instant = Instant.now()): Boolean =
        offerExpiresAt != null && now.isBefore(offerExpiresAt) && discountPercent > 0.0

    fun secondsUntilExpiry(now: Instant = Instant.now()): Long =
        offerExpiresAt?.let { maxOf(0L, it.epochSecond - now.epochSecond) } ?: 0L
}
