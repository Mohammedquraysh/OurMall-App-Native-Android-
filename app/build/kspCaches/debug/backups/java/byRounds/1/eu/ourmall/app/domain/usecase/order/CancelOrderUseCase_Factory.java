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
public final class CancelOrderUseCase_Factory implements Factory<CancelOrderUseCase> {
  private final Provider<OrderRepository> repoProvider;

  public CancelOrderUseCase_Factory(Provider<OrderRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public CancelOrderUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static CancelOrderUseCase_Factory create(Provider<OrderRepository> repoProvider) {
    return new CancelOrderUseCase_Factory(repoProvider);
  }

  public static CancelOrderUseCase newInstance(OrderRepository repo) {
    return new CancelOrderUseCase(repo);
  }
}
