package eu.ourmall.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class ProductRepositoryImpl_Factory implements Factory<ProductRepositoryImpl> {
  private final Provider<MockProductApi> apiProvider;

  public ProductRepositoryImpl_Factory(Provider<MockProductApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public ProductRepositoryImpl get() {
    return newInstance(apiProvider.get());
  }

  public static ProductRepositoryImpl_Factory create(Provider<MockProductApi> apiProvider) {
    return new ProductRepositoryImpl_Factory(apiProvider);
  }

  public static ProductRepositoryImpl newInstance(MockProductApi api) {
    return new ProductRepositoryImpl(api);
  }
}
