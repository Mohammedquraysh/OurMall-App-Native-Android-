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
public final class RemoveFromCartUseCase_Factory implements Factory<RemoveFromCartUseCase> {
  private final Provider<CartRepository> repoProvider;

  public RemoveFromCartUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public RemoveFromCartUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static RemoveFromCartUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new RemoveFromCartUseCase_Factory(repoProvider);
  }

  public static RemoveFromCartUseCase newInstance(CartRepository repo) {
    return new RemoveFromCartUseCase(repo);
  }
}
