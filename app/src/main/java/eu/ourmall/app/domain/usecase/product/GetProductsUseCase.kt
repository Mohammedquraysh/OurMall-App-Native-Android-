package eu.ourmall.app.domain.usecase.product

import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.model.ProductFilter
import eu.ourmall.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(
        filter: ProductFilter,
        page: Int = 0,
        pageSize: Int = 20
    ): Flow<Result<List<Product>>> = repository.getProducts(filter, page, pageSize)
}