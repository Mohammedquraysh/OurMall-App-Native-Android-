package eu.ourmall.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.data.local.dao.CartDao;
import eu.ourmall.app.data.local.dao.PromoDao;
import eu.ourmall.app.data.remote.api.MockProductApi;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CartRepositoryImpl_Factory implements Factory<CartRepositoryImpl> {
  private final Provider<CartDao> cartDaoProvider;

  private final Provider<PromoDao> promoDaoProvider;

  private final Provider<MockProductApi> apiProvider;

  public CartRepositoryImpl_Factory(Provider<CartDao> cartDaoProvider,
      Provider<PromoDao> promoDaoProvider, Provider<MockProductApi> apiProvider) {
    this.cartDaoProvider = cartDaoProvider;
    this.promoDaoProvider = promoDaoProvider;
    this.apiProvider = apiProvider;
  }

  @Override
  public CartRepositoryImpl get() {
    return newInstance(cartDaoProvider.get(), promoDaoProvider.get(), apiProvider.get());
  }

  public static CartRepositoryImpl_Factory create(Provider<CartDao> cartDaoProvider,
      Provider<PromoDao> promoDaoProvider, Provider<MockProductApi> apiProvider) {
    return new CartRepositoryImpl_Factory(cartDaoProvider, promoDaoProvider, apiProvider);
  }

  public static CartRepositoryImpl newInstance(CartDao cartDao, PromoDao promoDao,
      MockProductApi api) {
    return new CartRepositoryImpl(cartDao, promoDao, api);
  }
}
