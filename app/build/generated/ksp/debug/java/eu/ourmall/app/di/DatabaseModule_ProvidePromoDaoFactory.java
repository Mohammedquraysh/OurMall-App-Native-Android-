package eu.ourmall.app.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import eu.ourmall.app.data.local.dao.PromoDao;
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
public final class DatabaseModule_ProvidePromoDaoFactory implements Factory<PromoDao> {
  private final Provider<OurMallDatabase> dbProvider;

  public DatabaseModule_ProvidePromoDaoFactory(Provider<OurMallDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PromoDao get() {
    return providePromoDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePromoDaoFactory create(Provider<OurMallDatabase> dbProvider) {
    return new DatabaseModule_ProvidePromoDaoFactory(dbProvider);
  }

  public static PromoDao providePromoDao(OurMallDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePromoDao(db));
  }
}
