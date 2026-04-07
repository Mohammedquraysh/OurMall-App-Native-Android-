package eu.ourmall.app.domain.usecase.product

import eu.ourmall.app.domain.model.Product
import eu.ourmall.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(id: String): Flow<Result<Product>> = repository.getProductById(id)
}