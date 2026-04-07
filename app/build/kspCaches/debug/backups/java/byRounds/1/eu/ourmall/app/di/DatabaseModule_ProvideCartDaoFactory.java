package eu.ourmall.app.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.data.local.dao.CartDao;
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
public final class DatabaseModule_ProvideCartDaoFactory implements Factory<CartDao> {
  private final Provider<OurMallDatabase> dbProvider;

  public DatabaseModule_ProvideCartDaoFactory(Provider<OurMallDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CartDao get() {
    return provideCartDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCartDaoFactory create(Provider<OurMallDatabase> dbProvider) {
    return new DatabaseModule_ProvideCartDaoFactory(dbProvider);
  }

  public static CartDao provideCartDao(OurMallDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCartDao(db));
  }
}
