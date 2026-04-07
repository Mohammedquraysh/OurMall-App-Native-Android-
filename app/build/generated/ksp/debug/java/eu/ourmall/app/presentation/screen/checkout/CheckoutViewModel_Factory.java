package eu.ourmall.app.presentation.screen.checkout;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.cart.ClearCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ValidateCartUseCase;
import eu.ourmall.app.domain.usecase.order.CreateOrderUseCase;
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
public final class CheckoutViewModel_Factory implements Factory<CheckoutViewModel> {
  private final Provider<ObserveCartUseCase> observeCartProvider;

  private final Provider<ValidateCartUseCase> validateCartProvider;

  private final Provider<CreateOrderUseCase> createOrderProvider;

  private final Provider<ClearCartUseCase> clearCartProvider;

  public CheckoutViewModel_Factory(Provider<ObserveCartUseCase> observeCartProvider,
      Provider<ValidateCartUseCase> validateCartProvider,
      Provider<CreateOrderUseCase> createOrderProvider,
      Provider<ClearCartUseCase> clearCartProvider) {
    this.observeCartProvider = observeCartProvider;
    this.validateCartProvider = validateCartProvider;
    this.createOrderProvider = createOrderProvider;
    this.clearCartProvider = clearCartProvider;
  }

  @Override
  public CheckoutViewModel get() {
    return newInstance(observeCartProvider.get(), validateCartProvider.get(), createOrderProvider.get(), clearCartProvider.get());
  }

  public static CheckoutViewModel_Factory create(Provider<ObserveCartUseCase> observeCartProvider,
      Provider<ValidateCartUseCase> validateCartProvider,
      Provider<CreateOrderUseCase> createOrderProvider,
      Provider<ClearCartUseCase> clearCartProvider) {
    return new CheckoutViewModel_Factory(observeCartProvider, validateCartProvider, createOrderProvider, clearCartProvider);
  }

  public static CheckoutViewModel newInstance(ObserveCartUseCase observeCart,
      ValidateCartUseCase validateCart, CreateOrderUseCase createOrder,
      ClearCartUseCase clearCart) {
    return new CheckoutViewModel(observeCart, validateCart, createOrder, clearCart);
  }
}
