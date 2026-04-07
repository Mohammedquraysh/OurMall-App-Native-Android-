package eu.ourmall.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import eu.ourmall.app.data.local.dao.CartDao
import eu.ourmall.app.data.local.dao.OrderDao
import eu.ourmall.app.data.local.dao.PromoDao
import eu.ourmall.app.data.local.entity.ActivePromoEntity
import eu.ourmall.app.data.local.entity.CartItemEntity
import eu.ourmall.app.data.local.entity.OrderEntity

@Database(
    entities = [CartItemEntity::class, OrderEntity::class, ActivePromoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class OurMallDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun promoDao(): PromoDao
}
