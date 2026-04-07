package eu.ourmall.app.presentation.screen.cart;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.cart.ApplyPromoCodeUseCase;
import eu.ourmall.app.domain.usecase.cart.ClearCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase;
import eu.ourmall.app.domain.usecase.cart.RemoveFromCartUseCase;
import eu.ourmall.app.domain.usecase.cart.UpdateCartQuantityUseCase;
import eu.ourmall.app.domain.usecase.cart.ValidateCartUseCase;
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
public final class CartViewModel_Factory implements Factory<CartViewModel> {
  private final Provider<ObserveCartUseCase> observeCartProvider;

  private final Provider<UpdateCartQuantityUseCase> updateQtyProvider;

  private final Provider<RemoveFromCartUseCase> removeItemProvider;

  private final Provider<ApplyPromoCodeUseCase> applyPromoProvider;

  private final Provider<ValidateCartUseCase> validateCartProvider;

  private final Provider<ClearCartUseCase> clearCartProvider;

  public CartViewModel_Factory(Provider<ObserveCartUseCase> observeCartProvider,
      Provider<UpdateCartQuantityUseCase> updateQtyProvider,
      Provider<RemoveFromCartUseCase> removeItemProvider,
      Provider<ApplyPromoCodeUseCase> applyPromoProvider,
      Provider<ValidateCartUseCase> validateCartProvider,
      Provider<ClearCartUseCase> clearCartProvider) {
    this.observeCartProvider = observeCartProvider;
    this.updateQtyProvider = updateQtyProvider;
    this.removeItemProvider = removeItemProvider;
    this.applyPromoProvider = applyPromoProvider;
    this.validateCartProvider = validateCartProvider;
    this.clearCartProvider = clearCartProvider;
  }

  @Override
  public CartViewModel get() {
    return newInstance(observeCartProvider.get(), updateQtyProvider.get(), removeItemProvider.get(), applyPromoProvider.get(), validateCartProvider.get(), clearCartProvider.get());
  }

  public static CartViewModel_Factory create(Provider<ObserveCartUseCase> observeCartProvider,
      Provider<UpdateCartQuantityUseCase> updateQtyProvider,
      Provider<RemoveFromCartUseCase> removeItemProvider,
      Provider<ApplyPromoCodeUseCase> applyPromoProvider,
      Provider<ValidateCartUseCase> validateCartProvider,
      Provider<ClearCartUseCase> clearCartProvider) {
    return new CartViewModel_Factory(observeCartProvider, updateQtyProvider, removeItemProvider, applyPromoProvider, validateCartProvider, clearCartProvider);
  }

  public static CartViewModel newInstance(ObserveCartUseCase observeCart,
      UpdateCartQuantityUseCase updateQty, RemoveFromCartUseCase removeItem,
      ApplyPromoCodeUseCase applyPromo, ValidateCartUseCase validateCart,
      ClearCartUseCase clearCart) {
    return new CartViewModel(observeCart, updateQty, removeItem, applyPromo, validateCart, clearCart);
  }
}
