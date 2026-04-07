package eu.ourmall.app.util

import java.text.NumberFormat
import java.util.Locale

fun Double.formatPrice(): String {
    val fmt = NumberFormat.getNumberInstance(Locale("en", "NG"))
    fmt.maximumFractionDigits = 0
    fmt.minimumFractionDigits = 0
    return "₦${fmt.format(this)}"
}

fun Long.formatCountdown(): String {
    val h = this / 3600
    val m = (this % 3600) / 60
    val s = this % 60
    return when {
        h > 0  -> "%02d:%02d:%02d".format(h, m, s)
        else   -> "%02d:%02d".format(m, s)
    }
}

fun String.ellipsize(max: Int) = if (length > max) take(max) + "…" else this




enum class CheckoutStep { REVIEW, PLACING, SUCCESS }
enum class StockStatus { IN_STOCK, LOW_STOCK, OUT_OF_STOCK }

enum class OrderItemStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}
enum class OrderStatus { PENDING, CONFIRMED, PARTIALLY_CANCELLED, CANCELLED, COMPLETED }


