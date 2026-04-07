package eu.ourmall.app.presentation.screen.order;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.order.GetOrdersUseCase;
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
public final class OrderListViewModel_Factory implements Factory<OrderListViewModel> {
  private final Provider<GetOrdersUseCase> getOrdersProvider;

  public OrderListViewModel_Factory(Provider<GetOrdersUseCase> getOrdersProvider) {
    this.getOrdersProvider = getOrdersProvider;
  }

  @Override
  public OrderListViewModel get() {
    return newInstance(getOrdersProvider.get());
  }

  public static OrderListViewModel_Factory create(Provider<GetOrdersUseCase> getOrdersProvider) {
    return new OrderListViewModel_Factory(getOrdersProvider);
  }

  public static OrderListViewModel newInstance(GetOrdersUseCase getOrders) {
    return new OrderListViewModel(getOrders);
  }
}
