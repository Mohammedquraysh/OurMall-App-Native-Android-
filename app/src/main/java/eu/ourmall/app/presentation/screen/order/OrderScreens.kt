package eu.ourmall.app.presentation.screen.order

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.ourmall.app.domain.model.*
import eu.ourmall.app.presentation.components.*
import eu.ourmall.app.presentation.theme.*
import eu.ourmall.app.util.OrderItemStatus
import eu.ourmall.app.util.OrderStatus
import eu.ourmall.app.util.formatPrice
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 ORDER LIST SCREEN
**/

@Composable
fun OrderListScreen(
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    viewModel: OrderListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { OurMallTopBar(title = "My Orders", onBack = onBack) }
    ) { padding ->
        when {
            state.isLoading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }

            state.orders.isEmpty() -> EmptyState(
                icon = Icons.Default.Receipt,
                title = "No Orders Yet",
                subtitle = "Your orders will appear here once you place them",
                action = { OurMallButton("Shop Now", onBack, icon = Icons.Default.Storefront) }
            )

            else -> LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(state.orders, key = { _, o -> o.id }) { index, order ->
                    AnimatedListItem(index = index) {
                        OrderCard(order = order, onClick = { onOrderClick(order.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        .withZone(ZoneId.systemDefault())

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(order.id, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold)
                    Text(formatter.format(order.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OrderStatusChip(order.status)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            /** Preview of first 2 items **/
            val previewItems = order.allItems.take(2)
            previewItems.forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    ProductImage(
                        url = item.imageUrl,
                        modifier = Modifier.size(40.dp).clip(MaterialTheme.shapes.small),
                    )
                    Column(Modifier.weight(1f)) {
                        Text(item.productName, style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                        Text("Qty: ${item.quantity} • ${item.lineTotal.formatPrice()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    OrderItemStatusChip(item.status)
                }
            }

            if (order.allItems.size > 2) {
                Text("+${order.allItems.size - 2} more items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp))
                    Text("${order.vendorOrders.size} vendor${if (order.vendorOrders.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(order.grandTotal.formatPrice(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun OrderStatusChip(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.PENDING              -> "Pending"    to OurMallAmber
        OrderStatus.CONFIRMED            -> "Confirmed"  to OurMallBlue
        OrderStatus.PARTIALLY_CANCELLED  -> "Part. Cancelled" to OurMallOrange
        OrderStatus.CANCELLED            -> "Cancelled"  to OurMallRed
        OrderStatus.COMPLETED            -> "Completed"  to OurMallGreen
    }
    Surface(shape = RoundedCornerShape(20.dp), color = color.copy(0.15f)) {
        Text(text, style = MaterialTheme.typography.labelSmall,
            color = color, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}







