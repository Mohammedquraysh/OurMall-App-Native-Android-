package eu.ourmall.app.domain.usecase.product

import eu.ourmall.app.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCategoriesUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<Result<List<String>>> = repository.getCategories()
}
