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
public final class CancelOrderItemUseCase_Factory implements Factory<CancelOrderItemUseCase> {
  private final Provider<OrderRepository> repoProvider;

  public CancelOrderItemUseCase_Factory(Provider<OrderRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public CancelOrderItemUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static CancelOrderItemUseCase_Factory create(Provider<OrderRepository> repoProvider) {
    return new CancelOrderItemUseCase_Factory(repoProvider);
  }

  public static CancelOrderItemUseCase newInstance(OrderRepository repo) {
    return new CancelOrderItemUseCase(repo);
  }
}
