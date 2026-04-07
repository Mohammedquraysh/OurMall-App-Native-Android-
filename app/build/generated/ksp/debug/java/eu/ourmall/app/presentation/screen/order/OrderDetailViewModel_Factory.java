package eu.ourmall.app.presentation.screen.order;

import androidx.lifecycle.SavedStateHandle;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.domain.usecase.order.CancelOrderItemUseCase;
import eu.ourmall.app.domain.usecase.order.CancelOrderUseCase;
import eu.ourmall.app.domain.usecase.order.GetOrderByIdUseCase;
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
public final class OrderDetailViewModel_Factory implements Factory<OrderDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<GetOrderByIdUseCase> getOrderByIdProvider;

  private final Provider<CancelOrderUseCase> cancelOrderProvider;

  private final Provider<CancelOrderItemUseCase> cancelItemProvider;

  public OrderDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetOrderByIdUseCase> getOrderByIdProvider,
      Provider<CancelOrderUseCase> cancelOrderProvider,
      Provider<CancelOrderItemUseCase> cancelItemProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.getOrderByIdProvider = getOrderByIdProvider;
    this.cancelOrderProvider = cancelOrderProvider;
    this.cancelItemProvider = cancelItemProvider;
  }

  @Override
  public OrderDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), getOrderByIdProvider.get(), cancelOrderProvider.get(), cancelItemProvider.get());
  }

  public static OrderDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<GetOrderByIdUseCase> getOrderByIdProvider,
      Provider<CancelOrderUseCase> cancelOrderProvider,
      Provider<CancelOrderItemUseCase> cancelItemProvider) {
    return new OrderDetailViewModel_Factory(savedStateHandleProvider, getOrderByIdProvider, cancelOrderProvider, cancelItemProvider);
  }

  public static OrderDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      GetOrderByIdUseCase getOrderById, CancelOrderUseCase cancelOrder,
      CancelOrderItemUseCase cancelItem) {
    return new OrderDetailViewModel(savedStateHandle, getOrderById, cancelOrder, cancelItem);
  }
}
