package eu.ourmall.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val createdAtEpoch: Long,
    val statusJson: String,  // serialised OrderStatus
    val vendorOrdersJson: String, // serialised List<VendorOrder> as JSON
    val cartLevelDiscountAmount: Double,
    val promoCode: String?,
)
