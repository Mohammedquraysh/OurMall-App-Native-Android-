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
public final class ClearCartUseCase_Factory implements Factory<ClearCartUseCase> {
  private final Provider<CartRepository> repoProvider;

  public ClearCartUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public ClearCartUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static ClearCartUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new ClearCartUseCase_Factory(repoProvider);
  }

  public static ClearCartUseCase newInstance(CartRepository repo) {
    return new ClearCartUseCase(repo);
  }
}
