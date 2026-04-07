package eu.ourmall.app.data.remote.dto

data class PromoCodeDto(
    val code: String,
    val discountPercent: Double,
    val description: String,
)
