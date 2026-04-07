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
public final class UpdateCartQuantityUseCase_Factory implements Factory<UpdateCartQuantityUseCase> {
  private final Provider<CartRepository> repoProvider;

  public UpdateCartQuantityUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public UpdateCartQuantityUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static UpdateCartQuantityUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new UpdateCartQuantityUseCase_Factory(repoProvider);
  }

  public static UpdateCartQuantityUseCase newInstance(CartRepository repo) {
    return new UpdateCartQuantityUseCase(repo);
  }
}
