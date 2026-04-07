package eu.ourmall.app.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.data.local.dao.OrderDao;
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
public final class OrderRepositoryImpl_Factory implements Factory<OrderRepositoryImpl> {
  private final Provider<OrderDao> orderDaoProvider;

  public OrderRepositoryImpl_Factory(Provider<OrderDao> orderDaoProvider) {
    this.orderDaoProvider = orderDaoProvider;
  }

  @Override
  public OrderRepositoryImpl get() {
    return newInstance(orderDaoProvider.get());
  }

  public static OrderRepositoryImpl_Factory create(Provider<OrderDao> orderDaoProvider) {
    return new OrderRepositoryImpl_Factory(orderDaoProvider);
  }

  public static OrderRepositoryImpl newInstance(OrderDao orderDao) {
    return new OrderRepositoryImpl(orderDao);
  }
}
