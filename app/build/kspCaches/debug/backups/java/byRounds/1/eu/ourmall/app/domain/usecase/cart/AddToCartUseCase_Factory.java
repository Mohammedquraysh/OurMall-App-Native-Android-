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
public final class AddToCartUseCase_Factory implements Factory<AddToCartUseCase> {
  private final Provider<CartRepository> repoProvider;

  public AddToCartUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public AddToCartUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static AddToCartUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new AddToCartUseCase_Factory(repoProvider);
  }

  public static AddToCartUseCase newInstance(CartRepository repo) {
    return new AddToCartUseCase(repo);
  }
}
