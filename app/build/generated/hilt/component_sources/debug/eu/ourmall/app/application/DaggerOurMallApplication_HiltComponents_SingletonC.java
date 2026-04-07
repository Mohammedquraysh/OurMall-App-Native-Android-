package eu.ourmall.app.application;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import eu.ourmall.app.MainActivity;
import eu.ourmall.app.data.local.dao.CartDao;
import eu.ourmall.app.data.local.dao.OrderDao;
import eu.ourmall.app.data.local.dao.PromoDao;
import eu.ourmall.app.data.local.database.OurMallDatabase;
import eu.ourmall.app.data.remote.api.MockProductApi;
import eu.ourmall.app.data.repository.CartRepositoryImpl;
import eu.ourmall.app.data.repository.OrderRepositoryImpl;
import eu.ourmall.app.data.repository.ProductRepositoryImpl;
import eu.ourmall.app.di.DatabaseModule_ProvideCartDaoFactory;
import eu.ourmall.app.di.DatabaseModule_ProvideDatabaseFactory;
import eu.ourmall.app.di.DatabaseModule_ProvideOrderDaoFactory;
import eu.ourmall.app.di.DatabaseModule_ProvidePromoDaoFactory;
import eu.ourmall.app.domain.usecase.cart.AddToCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ApplyPromoCodeUseCase;
import eu.ourmall.app.domain.usecase.cart.ClearCartUseCase;
import eu.ourmall.app.domain.usecase.cart.ObserveCartUseCase;
import eu.ourmall.app.domain.usecase.cart.RemoveFromCartUseCase;
import eu.ourmall.app.domain.usecase.cart.UpdateCartQuantityUseCase;
import eu.ourmall.app.domain.usecase.cart.ValidateCartUseCase;
import eu.ourmall.app.domain.usecase.order.CancelOrderItemUseCase;
import eu.ourmall.app.domain.usecase.order.CancelOrderUseCase;
import eu.ourmall.app.domain.usecase.order.CreateOrderUseCase;
import eu.ourmall.app.domain.usecase.order.GetOrderByIdUseCase;
import eu.ourmall.app.domain.usecase.order.GetOrdersUseCase;
import eu.ourmall.app.domain.usecase.product.GetCategoriesUseCase;
import eu.ourmall.app.domain.usecase.product.GetProductByIdUseCase;
import eu.ourmall.app.domain.usecase.product.GetProductsUseCase;
import eu.ourmall.app.presentation.screen.cart.CartViewModel;
import eu.ourmall.app.presentation.screen.cart.CartViewModel_HiltModules;
import eu.ourmall.app.presentation.screen.checkout.CheckoutViewModel;
import eu.ourmall.app.presentation.screen.checkout.CheckoutViewModel_HiltModules;
import eu.ourmall.app.presentation.screen.order.OrderDetailViewModel;
import eu.ourmall.app.presentation.screen.order.OrderDetailViewModel_HiltModules;
import eu.ourmall.app.presentation.screen.order.OrderListViewModel;
import eu.ourmall.app.presentation.screen.order.OrderListViewModel_HiltModules;
import eu.ourmall.app.presentation.screen.products.ProductDetailViewModel;
import eu.ourmall.app.presentation.screen.products.ProductDetailViewModel_HiltModules;
import eu.ourmall.app.presentation.screen.products.ProductListViewModel;
import eu.ourmall.app.presentation.screen.products.ProductListViewModel_HiltModules;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerOurMallApplication_HiltComponents_SingletonC {
  private DaggerOurMallApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public OurMallApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements OurMallApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements OurMallApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements OurMallApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements OurMallApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements OurMallApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements OurMallApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements OurMallApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public OurMallApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends OurMallApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends OurMallApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends OurMallApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends OurMallApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(6).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_cart_CartViewModel, CartViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel, CheckoutViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_order_OrderDetailViewModel, OrderDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_order_OrderListViewModel, OrderListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_products_ProductDetailViewModel, ProductDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_products_ProductListViewModel, ProductListViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String eu_ourmall_app_presentation_screen_cart_CartViewModel = "eu.ourmall.app.presentation.screen.cart.CartViewModel";

      static String eu_ourmall_app_presentation_screen_order_OrderListViewModel = "eu.ourmall.app.presentation.screen.order.OrderListViewModel";

      static String eu_ourmall_app_presentation_screen_products_ProductListViewModel = "eu.ourmall.app.presentation.screen.products.ProductListViewModel";

      static String eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel = "eu.ourmall.app.presentation.screen.checkout.CheckoutViewModel";

      static String eu_ourmall_app_presentation_screen_products_ProductDetailViewModel = "eu.ourmall.app.presentation.screen.products.ProductDetailViewModel";

      static String eu_ourmall_app_presentation_screen_order_OrderDetailViewModel = "eu.ourmall.app.presentation.screen.order.OrderDetailViewModel";

      @KeepFieldType
      CartViewModel eu_ourmall_app_presentation_screen_cart_CartViewModel2;

      @KeepFieldType
      OrderListViewModel eu_ourmall_app_presentation_screen_order_OrderListViewModel2;

      @KeepFieldType
      ProductListViewModel eu_ourmall_app_presentation_screen_products_ProductListViewModel2;

      @KeepFieldType
      CheckoutViewModel eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel2;

      @KeepFieldType
      ProductDetailViewModel eu_ourmall_app_presentation_screen_products_ProductDetailViewModel2;

      @KeepFieldType
      OrderDetailViewModel eu_ourmall_app_presentation_screen_order_OrderDetailViewModel2;
    }
  }

  private static final class ViewModelCImpl extends OurMallApplication_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CartViewModel> cartViewModelProvider;

    private Provider<CheckoutViewModel> checkoutViewModelProvider;

    private Provider<OrderDetailViewModel> orderDetailViewModelProvider;

    private Provider<OrderListViewModel> orderListViewModelProvider;

    private Provider<ProductDetailViewModel> productDetailViewModelProvider;

    private Provider<ProductListViewModel> productListViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private ObserveCartUseCase observeCartUseCase() {
      return new ObserveCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private UpdateCartQuantityUseCase updateCartQuantityUseCase() {
      return new UpdateCartQuantityUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private RemoveFromCartUseCase removeFromCartUseCase() {
      return new RemoveFromCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private ApplyPromoCodeUseCase applyPromoCodeUseCase() {
      return new ApplyPromoCodeUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private ValidateCartUseCase validateCartUseCase() {
      return new ValidateCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private ClearCartUseCase clearCartUseCase() {
      return new ClearCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private CreateOrderUseCase createOrderUseCase() {
      return new CreateOrderUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private GetOrderByIdUseCase getOrderByIdUseCase() {
      return new GetOrderByIdUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private CancelOrderUseCase cancelOrderUseCase() {
      return new CancelOrderUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private CancelOrderItemUseCase cancelOrderItemUseCase() {
      return new CancelOrderItemUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private GetOrdersUseCase getOrdersUseCase() {
      return new GetOrdersUseCase(singletonCImpl.orderRepositoryImplProvider.get());
    }

    private GetProductByIdUseCase getProductByIdUseCase() {
      return new GetProductByIdUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private AddToCartUseCase addToCartUseCase() {
      return new AddToCartUseCase(singletonCImpl.cartRepositoryImplProvider.get());
    }

    private GetProductsUseCase getProductsUseCase() {
      return new GetProductsUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    private GetCategoriesUseCase getCategoriesUseCase() {
      return new GetCategoriesUseCase(singletonCImpl.productRepositoryImplProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.cartViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.checkoutViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.orderDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.orderListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.productDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.productListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_cart_CartViewModel, ((Provider) cartViewModelProvider)).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel, ((Provider) checkoutViewModelProvider)).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_order_OrderDetailViewModel, ((Provider) orderDetailViewModelProvider)).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_order_OrderListViewModel, ((Provider) orderListViewModelProvider)).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_products_ProductDetailViewModel, ((Provider) productDetailViewModelProvider)).put(LazyClassKeyProvider.eu_ourmall_app_presentation_screen_products_ProductListViewModel, ((Provider) productListViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String eu_ourmall_app_presentation_screen_cart_CartViewModel = "eu.ourmall.app.presentation.screen.cart.CartViewModel";

      static String eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel = "eu.ourmall.app.presentation.screen.checkout.CheckoutViewModel";

      static String eu_ourmall_app_presentation_screen_order_OrderDetailViewModel = "eu.ourmall.app.presentation.screen.order.OrderDetailViewModel";

      static String eu_ourmall_app_presentation_screen_order_OrderListViewModel = "eu.ourmall.app.presentation.screen.order.OrderListViewModel";

      static String eu_ourmall_app_presentation_screen_products_ProductListViewModel = "eu.ourmall.app.presentation.screen.products.ProductListViewModel";

      static String eu_ourmall_app_presentation_screen_products_ProductDetailViewModel = "eu.ourmall.app.presentation.screen.products.ProductDetailViewModel";

      @KeepFieldType
      CartViewModel eu_ourmall_app_presentation_screen_cart_CartViewModel2;

      @KeepFieldType
      CheckoutViewModel eu_ourmall_app_presentation_screen_checkout_CheckoutViewModel2;

      @KeepFieldType
      OrderDetailViewModel eu_ourmall_app_presentation_screen_order_OrderDetailViewModel2;

      @KeepFieldType
      OrderListViewModel eu_ourmall_app_presentation_screen_order_OrderListViewModel2;

      @KeepFieldType
      ProductListViewModel eu_ourmall_app_presentation_screen_products_ProductListViewModel2;

      @KeepFieldType
      ProductDetailViewModel eu_ourmall_app_presentation_screen_products_ProductDetailViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // eu.ourmall.app.presentation.screen.cart.CartViewModel 
          return (T) new CartViewModel(viewModelCImpl.observeCartUseCase(), viewModelCImpl.updateCartQuantityUseCase(), viewModelCImpl.removeFromCartUseCase(), viewModelCImpl.applyPromoCodeUseCase(), viewModelCImpl.validateCartUseCase(), viewModelCImpl.clearCartUseCase());

          case 1: // eu.ourmall.app.presentation.screen.checkout.CheckoutViewModel 
          return (T) new CheckoutViewModel(viewModelCImpl.observeCartUseCase(), viewModelCImpl.validateCartUseCase(), viewModelCImpl.createOrderUseCase(), viewModelCImpl.clearCartUseCase());

          case 2: // eu.ourmall.app.presentation.screen.order.OrderDetailViewModel 
          return (T) new OrderDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getOrderByIdUseCase(), viewModelCImpl.cancelOrderUseCase(), viewModelCImpl.cancelOrderItemUseCase());

          case 3: // eu.ourmall.app.presentation.screen.order.OrderListViewModel 
          return (T) new OrderListViewModel(viewModelCImpl.getOrdersUseCase());

          case 4: // eu.ourmall.app.presentation.screen.products.ProductDetailViewModel 
          return (T) new ProductDetailViewModel(viewModelCImpl.savedStateHandle, viewModelCImpl.getProductByIdUseCase(), viewModelCImpl.addToCartUseCase(), viewModelCImpl.observeCartUseCase());

          case 5: // eu.ourmall.app.presentation.screen.products.ProductListViewModel 
          return (T) new ProductListViewModel(viewModelCImpl.getProductsUseCase(), viewModelCImpl.getCategoriesUseCase(), viewModelCImpl.addToCartUseCase(), viewModelCImpl.observeCartUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends OurMallApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends OurMallApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends OurMallApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<OurMallDatabase> provideDatabaseProvider;

    private Provider<CartDao> provideCartDaoProvider;

    private Provider<PromoDao> providePromoDaoProvider;

    private Provider<MockProductApi> mockProductApiProvider;

    private Provider<CartRepositoryImpl> cartRepositoryImplProvider;

    private Provider<OrderDao> provideOrderDaoProvider;

    private Provider<OrderRepositoryImpl> orderRepositoryImplProvider;

    private Provider<ProductRepositoryImpl> productRepositoryImplProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<OurMallDatabase>(singletonCImpl, 2));
      this.provideCartDaoProvider = DoubleCheck.provider(new SwitchingProvider<CartDao>(singletonCImpl, 1));
      this.providePromoDaoProvider = DoubleCheck.provider(new SwitchingProvider<PromoDao>(singletonCImpl, 3));
      this.mockProductApiProvider = DoubleCheck.provider(new SwitchingProvider<MockProductApi>(singletonCImpl, 4));
      this.cartRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<CartRepositoryImpl>(singletonCImpl, 0));
      this.provideOrderDaoProvider = DoubleCheck.provider(new SwitchingProvider<OrderDao>(singletonCImpl, 6));
      this.orderRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<OrderRepositoryImpl>(singletonCImpl, 5));
      this.productRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ProductRepositoryImpl>(singletonCImpl, 7));
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    @Override
    public void injectOurMallApplication(OurMallApplication ourMallApplication) {
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // eu.ourmall.app.data.repository.CartRepositoryImpl 
          return (T) new CartRepositoryImpl(singletonCImpl.provideCartDaoProvider.get(), singletonCImpl.providePromoDaoProvider.get(), singletonCImpl.mockProductApiProvider.get());

          case 1: // eu.ourmall.app.data.local.dao.CartDao 
          return (T) DatabaseModule_ProvideCartDaoFactory.provideCartDao(singletonCImpl.provideDatabaseProvider.get());

          case 2: // eu.ourmall.app.data.local.database.OurMallDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // eu.ourmall.app.data.local.dao.PromoDao 
          return (T) DatabaseModule_ProvidePromoDaoFactory.providePromoDao(singletonCImpl.provideDatabaseProvider.get());

          case 4: // eu.ourmall.app.data.remote.api.MockProductApi 
          return (T) new MockProductApi();

          case 5: // eu.ourmall.app.data.repository.OrderRepositoryImpl 
          return (T) new OrderRepositoryImpl(singletonCImpl.provideOrderDaoProvider.get());

          case 6: // eu.ourmall.app.data.local.dao.OrderDao 
          return (T) DatabaseModule_ProvideOrderDaoFactory.provideOrderDao(singletonCImpl.provideDatabaseProvider.get());

          case 7: // eu.ourmall.app.data.repository.ProductRepositoryImpl 
          return (T) new ProductRepositoryImpl(singletonCImpl.mockProductApiProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
