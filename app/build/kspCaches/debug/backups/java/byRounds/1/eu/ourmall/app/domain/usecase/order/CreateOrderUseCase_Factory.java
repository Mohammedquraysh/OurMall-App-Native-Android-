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
public final class CreateOrderUseCase_Factory implements Factory<CreateOrderUseCase> {
  private final Provider<OrderRepository> repoProvider;

  public CreateOrderUseCase_Factory(Provider<OrderRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public CreateOrderUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static CreateOrderUseCase_Factory create(Provider<OrderRepository> repoProvider) {
    return new CreateOrderUseCase_Factory(repoProvider);
  }

  public static CreateOrderUseCase newInstance(OrderRepository repo) {
    return new CreateOrderUseCase(repo);
  }
}
