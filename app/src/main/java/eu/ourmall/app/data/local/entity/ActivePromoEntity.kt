package eu.ourmall.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "promo_codes")
data class ActivePromoEntity(
    @PrimaryKey val id: Int = 0,     // singleton row
    val code: String,
    val discountPercent: Double,
    val description: String,
)

