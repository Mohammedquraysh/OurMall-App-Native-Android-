package eu.ourmall.app.domain.usecase.order;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.repository.OrderRepository;
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
public final class GetOrderByIdUseCase_Factory implements Factory<GetOrderByIdUseCase> {
  private final Provider<OrderRepository> repoProvider;

  public GetOrderByIdUseCase_Factory(Provider<OrderRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GetOrderByIdUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static GetOrderByIdUseCase_Factory create(Provider<OrderRepository> repoProvider) {
    return new GetOrderByIdUseCase_Factory(repoProvider);
  }

  public static GetOrderByIdUseCase newInstance(OrderRepository repo) {
    return new GetOrderByIdUseCase(repo);
  }
}
