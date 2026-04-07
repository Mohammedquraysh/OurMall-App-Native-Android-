package eu.ourmall.app.data.remote.dto


data class PagedResponse<T>(
    val items: List<T>,
    val totalCount: Int,
    val page: Int,
    val pageSize: Int,
    val hasNextPage: Boolean,
)