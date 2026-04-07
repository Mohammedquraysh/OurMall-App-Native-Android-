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
public final class GetOrdersUseCase_Factory implements Factory<GetOrdersUseCase> {
  private final Provider<OrderRepository> repoProvider;

  public GetOrdersUseCase_Factory(Provider<OrderRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public GetOrdersUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static GetOrdersUseCase_Factory create(Provider<OrderRepository> repoProvider) {
    return new GetOrdersUseCase_Factory(repoProvider);
  }

  public static GetOrdersUseCase newInstance(OrderRepository repo) {
    return new GetOrdersUseCase(repo);
  }
}
