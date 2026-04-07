package eu.ourmall.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.ourmall.app.data.local.dao.CartDao
import eu.ourmall.app.data.local.dao.OrderDao
import eu.ourmall.app.data.local.dao.PromoDao
import eu.ourmall.app.data.local.database.OurMallDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): OurMallDatabase =
        Room.databaseBuilder(ctx, OurMallDatabase::class.java, "ourmall.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides @Singleton
    fun provideCartDao(db: OurMallDatabase): CartDao = db.cartDao()

    @Provides @Singleton
    fun provideOrderDao(db: OurMallDatabase): OrderDao = db.orderDao()

    @Provides @Singleton
    fun providePromoDao(db: OurMallDatabase): PromoDao = db.promoDao()
}
