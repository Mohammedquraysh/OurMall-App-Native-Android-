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
public final class ApplyPromoCodeUseCase_Factory implements Factory<ApplyPromoCodeUseCase> {
  private final Provider<CartRepository> repoProvider;

  public ApplyPromoCodeUseCase_Factory(Provider<CartRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public ApplyPromoCodeUseCase get() {
    return newInstance(repoProvider.get());
  }

  public static ApplyPromoCodeUseCase_Factory create(Provider<CartRepository> repoProvider) {
    return new ApplyPromoCodeUseCase_Factory(repoProvider);
  }

  public static ApplyPromoCodeUseCase newInstance(CartRepository repo) {
    return new ApplyPromoCodeUseCase(repo);
  }
}
