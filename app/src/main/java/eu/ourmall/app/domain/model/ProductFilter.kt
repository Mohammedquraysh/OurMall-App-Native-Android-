package eu.ourmall.app.domain.model

import eu.ourmall.app.util.StockStatus

data class ProductFilter(
    val searchQuery: String = "",
    val category: String? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val stockStatus: StockStatus? = null,
)

