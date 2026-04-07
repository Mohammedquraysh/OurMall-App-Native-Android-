package eu.ourmall.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.ourmall.app.data.repository.CartRepositoryImpl
import eu.ourmall.app.data.repository.OrderRepositoryImpl
import eu.ourmall.app.data.repository.ProductRepositoryImpl
import eu.ourmall.app.domain.repository.CartRepository
import eu.ourmall.app.domain.repository.OrderRepository
import eu.ourmall.app.domain.repository.ProductRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
}