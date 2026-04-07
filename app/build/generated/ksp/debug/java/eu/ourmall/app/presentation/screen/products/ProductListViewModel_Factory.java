package eu.ourmall.app.presentation.screen.products;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.cart.AddToCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase;
import eu.ourmall.app.domain.usecase.product.GetCategoriesUseCase;
import eu.ourmall.app.domain.usecase.product.GetProductsUseCase;
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
public final class ProductListViewModel_Factory implements Factory<ProductListViewModel> {
  private final Provider<GetProductsUseCase> getProductsProvider;

  private final Provider<GetCategoriesUseCase> getCategoriesProvider;

  private final Provider<AddToCartUseCase> addToCartProvider;

  private final Provider<ObserveCartUseCase> observeCartProvider;

  public ProductListViewModel_Factory(Provider<GetProductsUseCase> getProductsProvider,
      Provider<GetCategoriesUseCase> getCategoriesProvider,
      Provider<AddToCartUseCase> addToCartProvider,
      Provider<ObserveCartUseCase> observeCartProvider) {
    this.getProductsProvider = getProductsProvider;
    this.getCategoriesProvider = getCategoriesProvider;
    this.addToCartProvider = addToCartProvider;
    this.observeCartProvider = observeCartProvider;
  }

  @Override
  public ProductListViewModel get() {
    return newInstance(getProductsProvider.get(), getCategoriesProvider.get(), addToCartProvider.get(), observeCartProvider.get());
  }

  public static ProductListViewModel_Factory create(
      Provider<GetProductsUseCase> getProductsProvider,
      Provider<GetCategoriesUseCase> getCategoriesProvider,
      Provider<AddToCartUseCase> addToCartProvider,
      Provider<ObserveCartUseCase> observeCartProvider) {
    return new ProductListViewModel_Factory(getProductsProvider, getCategoriesProvider, addToCartProvider, observeCartProvider);
  }

  public static ProductListViewModel newInstance(GetProductsUseCase getProducts,
      GetCategoriesUseCase getCategories, AddToCartUseCase addToCart,
      ObserveCartUseCase observeCart) {
    return new ProductListViewModel(getProducts, getCategories, addToCart, observeCart);
  }
}
