package eu.ourmall.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val productName: String,
    val imageUrl: String,
    val originalPrice: Double,
    val discountPercent: Double,
    val offerExpiresAtEpoch: Long?,
    val vendorId: String,
    val vendorName: String,
    val category: String,
    val stockQuantity: Int,
    val quantity: Int,
    val snapshotPrice: Double,
    val appliedProductDiscount: Double,
    val addedAt: Long = System.currentTimeMillis(),
)