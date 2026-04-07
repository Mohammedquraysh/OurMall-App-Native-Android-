package eu.ourmall.app.domain.usecase.cart;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.repository.CartRepository;
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
public final class ObserveCartUseCase_Factory implements Factory<ObserveCartUseCase> {
  private final Provider<CartRepository> repoProvider;

  public ObserveCartUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public ObserveCartUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static ObserveCartUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new ObserveCartUseCase_Factory(repoProvider);
  }

  public static ObserveCartUseCase newInstance(CartRepository repo) {
    return new ObserveCartUseCase(repo);
  }
}
