package eu.ourmall.app.domain.repository

import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.model.ProductFilter
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(filter: ProductFilter, page: Int, pageSize: Int): Flow<Result<List<Product>>>
    fun getProductById(id: String): Flow<Result<Product>>
    fun getCategories(): Flow<Result<List<String>>>
    suspend fun refreshProducts()
}