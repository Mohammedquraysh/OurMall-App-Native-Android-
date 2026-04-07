package eu.ourmall.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.ourmall.app.data.local.entity.ActivePromoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PromoDao {
    @Query("SELECT * FROM promo_codes WHERE id = 0")
    fun observe(): Flow<ActivePromoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun set(promo: ActivePromoEntity)

    @Query("DELETE FROM promo_codes WHERE id = 0")
    suspend fun clear()
}