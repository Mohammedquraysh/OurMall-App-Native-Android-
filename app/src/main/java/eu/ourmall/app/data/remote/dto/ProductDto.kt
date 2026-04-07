package eu.ourmall.app.data.remote.dto

data class ProductDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val originalPrice: Double,
    val discountPercent: Double,
    val offerExpiresAtEpoch: Long?, // Unix epoch seconds, null = no offer
    val vendorId: String,
    val vendorName: String,
    val category: String,
    val stockQuantity: Int,
)