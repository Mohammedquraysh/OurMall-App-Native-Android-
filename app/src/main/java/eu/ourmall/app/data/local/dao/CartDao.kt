package eu.ourmall.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.ourmall.app.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addedAt ASC")
    fun observeAll(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :id")
    suspend fun getById(id: String): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :qty WHERE productId = :id")
    suspend fun updateQuantity(id: String, qty: Int)

    @Query("DELETE FROM cart_items WHERE productId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearAll()

    @Query("UPDATE cart_items SET snapshotPrice = :price, appliedProductDiscount = :discount, stockQuantity = :stock, offerExpiresAtEpoch = :expiresAt WHERE productId = :id")
    suspend fun updatePriceAndStock(id: String, price: Double, discount: Double, stock: Int, expiresAt: Long?)
}