package eu.ourmall.app.domain.usecase.product;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.repository.ProductRepository;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class GetProductByIdUseCase_Factory implements Factory<GetProductByIdUseCase> {
  private final Provider<ProductRepository> repositoryProvider;

  public GetProductByIdUseCase_Factory(Provider<ProductRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetProductByIdUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetProductByIdUseCase_Factory create(
      Provider<ProductRepository> repositoryProvider) {
    return new GetProductByIdUseCase_Factory(repositoryProvider);
  }

  public static GetProductByIdUseCase newInstance(ProductRepository repository) {
    return new GetProductByIdUseCase(repository);
  }
}
