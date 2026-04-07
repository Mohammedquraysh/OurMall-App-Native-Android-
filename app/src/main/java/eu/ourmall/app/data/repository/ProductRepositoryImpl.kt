package eu.ourmall.app.data.repository

import eu.ourmall.app.data.remote.api.MockProductApi
import eu.ourmall.app.data.remote.dto.ApiResponse
import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.model.ProductFilter
import eu.ourmall.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val api: MockProductApi,
) : ProductRepository {

    override fun getProducts(
        filter: ProductFilter,
        page: Int,
        pageSize: Int
    ): Flow<Result<List<Product>>> = flow {
        try {
            val response = api.getProducts(
                query = filter.searchQuery,
                category = filter.category,
                minPrice = filter.minPrice,
                maxPrice = filter.maxPrice,
                stockFilter = filter.stockStatus?.name,
                page = page,
                pageSize = pageSize,
            )
            when (response) {
                is ApiResponse.Success -> emit(Result.success(response.data.items.map { it.toDomain() }))
                is ApiResponse.Error -> emit(Result.failure(Exception(response.message)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getProductById(id: String): Flow<Result<Product>> = flow {
        try {
            when (val r = api.getProductById(id)) {
                is ApiResponse.Success -> emit(Result.success(r.data.toDomain()))
                is ApiResponse.Error -> emit(Result.failure(Exception(r.message)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getCategories(): Flow<Result<List<String>>> = flow {
        try {
            when (val r = api.getCategories()) {
                is ApiResponse.Success -> emit(Result.success(r.data))
                is ApiResponse.Error -> emit(Result.failure(Exception(r.message)))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun refreshProducts() {
        /** In a real app this would trigger a sync worker **/
    }
}
