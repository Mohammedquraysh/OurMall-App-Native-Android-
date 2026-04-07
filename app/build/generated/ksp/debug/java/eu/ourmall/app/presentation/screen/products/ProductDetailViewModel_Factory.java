package eu.ourmall.app.presentation.screen.products;

import androidx.lifecycle.SavedStateHandle;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.cart.AddToCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase;
import eu.ourmall.app.domain.usecase.product.GetProductByIdUseCase;
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
public final class ProductDetailViewModel_Factory implements Factory<ProductDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetProductByIdUseCase> getProductByIdProvider;

  private final Provider<AddToCartUseCase> addToCartProvider;

  private final Provider<ObserveCartUseCase> observeCartProvider;

  public ProductDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetProductByIdUseCase> getProductByIdProvider,
      Provider<AddToCartUseCase> addToCartProvider,
      Provider<ObserveCartUseCase> observeCartProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getProductByIdProvider = getProductByIdProvider;
    this.addToCartProvider = addToCartProvider;
    this.observeCartProvider = observeCartProvider;
  }

  @Override
  public ProductDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getProductByIdProvider.get(), addToCartProvider.get(), observeCartProvider.get());
  }

  public static ProductDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetProductByIdUseCase> getProductByIdProvider,
      Provider<AddToCartUseCase> addToCartProvider,
      Provider<ObserveCartUseCase> observeCartProvider) {
    return new ProductDetailViewModel_Factory(savedStateHandleProvider, getProductByIdProvider, addToCartProvider, observeCartProvider);
  }

  public static ProductDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetProductByIdUseCase getProductById, AddToCartUseCase addToCart,
      ObserveCartUseCase observeCart) {
    return new ProductDetailViewModel(savedStateHandle, getProductById, addToCart, observeCart);
  }
}
