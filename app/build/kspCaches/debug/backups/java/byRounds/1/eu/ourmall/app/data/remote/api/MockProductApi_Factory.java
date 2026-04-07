package eu.ourmall.app.data.remote.api;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class MockProductApi_Factory implements Factory<MockProductApi> {
  @Override
  public MockProductApi get() {
    return newInstance();
  }

  public static MockProductApi_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MockProductApi newInstance() {
    return new MockProductApi();
  }

  private static final class InstanceHolder {
    private static final MockProductApi_Factory INSTANCE = new MockProductApi_Factory();
  }
}
