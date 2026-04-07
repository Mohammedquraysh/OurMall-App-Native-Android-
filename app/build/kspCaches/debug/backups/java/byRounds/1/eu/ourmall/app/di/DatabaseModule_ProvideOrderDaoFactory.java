package eu.ourmall.app.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.data.local.dao.OrderDao;
import eu.ourmall.app.data.local.database.OurMallDatabase;
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
public final class DatabaseModule_ProvideOrderDaoFactory implements Factory<OrderDao> {
  private final Provider<OurMallDatabase> dbProvider;

  public DatabaseModule_ProvideOrderDaoFactory(Provider<OurMallDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public OrderDao get() {
    return provideOrderDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideOrderDaoFactory create(Provider<OurMallDatabase> dbProvider) {
    return new DatabaseModule_ProvideOrderDaoFactory(dbProvider);
  }

  public static OrderDao provideOrderDao(OurMallDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideOrderDao(db));
  }
}
