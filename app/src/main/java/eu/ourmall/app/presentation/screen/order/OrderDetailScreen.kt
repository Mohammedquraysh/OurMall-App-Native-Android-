package eu.ourmall.app.presentation.screen.order

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.ourmall.app.domain.model.Order
import eu.ourmall.app.domain.model.OrderItem
import eu.ourmall.app.domain.model.VendorOrder
import eu.ourmall.app.presentation.components.EmptyState
import eu.ourmall.app.presentation.components.OrderItemStatusChip
import eu.ourmall.app.presentation.components.OurMallTopBar
import eu.ourmall.app.presentation.components.ProductImage
import eu.ourmall.app.presentation.theme.OurMallAmber
import eu.ourmall.app.presentation.theme.OurMallGreen
import eu.ourmall.app.presentation.theme.OurMallRed
import eu.ourmall.app.presentation.theme.animatedDouble
import eu.ourmall.app.util.OrderItemStatus
import eu.ourmall.app.util.formatPrice
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
// ORDER DETAIL SCREEN
**/
@Composable
fun OrderDetailScreen(
    orderId: String,
    onBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showCancelDialog by remember { mutableStateOf(false) }
    var cancelItemId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.cancelSuccess) {
        state.cancelSuccess?.let { snackbarHostState.showSnackbar(it); viewModel.clearSuccess() }
    }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it); viewModel.clearError() }
    }

    /** Full order cancel dialog **/
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            icon = { Icon(Icons.Default.Cancel, null, tint = OurMallRed) },
            title = { Text("Cancel Order?", fontWeight = FontWeight.Bold) },
            text = { Text("All cancellable items will be cancelled and refunded. This cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { showCancelDialog = false; viewModel.cancelFullOrder() },
                    colors = ButtonDefaults.buttonColors(containerColor = OurMallRed)
                ) { Text("Cancel Order") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCancelDialog = false }) { Text("Keep Order") }
            },
            shape = MaterialTheme.shapes.large,
        )
    }

    /** Single item cancel dialog **/
    cancelItemId?.let { itemId ->
        AlertDialog(
            onDismissRequest = { cancelItemId = null },
            icon = { Icon(Icons.Default.RemoveShoppingCart, null, tint = OurMallAmber) },
            title = { Text("Cancel Item?", fontWeight = FontWeight.Bold) },
            text = { Text("This item will be cancelled and refunded. Other items won't be affected.") },
            confirmButton = {
                Button(
                    onClick = { cancelItemId = null; viewModel.cancelSingleItem(itemId) },
                    colors = ButtonDefaults.buttonColors(containerColor = OurMallAmber)
                ) { Text("Cancel Item") }
            },
            dismissButton = {
                OutlinedButton(onClick = { cancelItemId = null }) { Text("Keep Item") }
            },
            shape = MaterialTheme.shapes.large,
        )
    }

    Scaffold(
        topBar = {
            OurMallTopBar(
                title = state.order?.id ?: "Order Details",
                onBack = onBack,
                actions = {
                    /** Cancel entire order button (only if any items are cancellable) **/
                    if (state.order?.allItems?.any { it.canBeCancelled } == true) {
                        TextButton(
                            onClick = { showCancelDialog = true },
                            enabled = !state.isCancelling,
                        ) {
                            Text("Cancel Order", color = OurMallRed,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            state.order == null -> EmptyState(
                icon = Icons.Default.ErrorOutline,
                title = "Order Not Found",
                subtitle = "This order could not be loaded",
                action = { OutlinedButton(onClick = onBack) { Text("Go Back") } }
            )
            else -> {
                val order = state.order!!
                val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                    .withZone(ZoneId.systemDefault())

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp,
                        top = padding.calculateTopPadding() + 8.dp,
                        bottom = 32.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    /** Order header card **/
                    item {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        ) {
                            Column(
                                Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("Order Date",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(formatter.format(order.createdAt),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold)
                                    }
                                    OrderStatusChip(order.status)
                                }
                                if (order.totalRefunded > 0) {
                                    Surface(shape = RoundedCornerShape(8.dp),
                                        color = OurMallGreen.copy(0.1f)) {
                                        Row(
                                            Modifier.fillMaxWidth().padding(10.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Replay, null,
                                                tint = OurMallGreen,
                                                modifier = Modifier.size(16.dp))
                                            Text("Total Refunded: ${order.totalRefunded.formatPrice()}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = OurMallGreen,
                                                fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /** Status flow stepper **/
                    item {
                        StatusStepper(order = order)
                    }

                    /** Vendor sub-orders **/
                    order.vendorOrders.forEach { vendorOrder ->
                        item(key = "vendor_${vendorOrder.vendorId}") {
                            VendorOrderCard(
                                vendorOrder = vendorOrder,
                                isCancelling = state.isCancelling,
                                onCancelItem = { cancelItemId = it },
                            )
                        }
                    }

                    /** Financial summary **/
                    item {
                        FinancialSummaryCard(order = order)
                    }
                }
            }
        }
    }
}

/** Status Stepper **/
@Composable
private fun StatusStepper(order: Order) {
    val steps = listOf(
        OrderItemStatus.PENDING,
        OrderItemStatus.CONFIRMED,
        OrderItemStatus.SHIPPED,
        OrderItemStatus.DELIVERED,
    )

    /** Determine the "most advanced" non-cancelled status **/
    val maxStatus = order.activeVendorOrders
        .flatMap { it.activeItems }
        .mapNotNull { item ->
            steps.indexOf(item.status).takeIf { it >= 0 }
        }
        .maxOrNull() ?: 0

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Fulfilment Status", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                steps.forEachIndexed { index, step ->
                    val isActive = index <= maxStatus
                    val isCurrent = index == maxStatus

                    /** Node **/
                    val nodeScale by animateFloatAsState(
                        targetValue = if (isCurrent) 1.2f else 1f,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy),
                        label = "step_scale_$index"
                    )
                    Box(
                        Modifier.size(28.dp).scale(nodeScale)
                            .clip(CircleShape)
                            .background(
                                if (isActive) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline.copy(0.4f)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isActive && !isCurrent) {
                            Icon(Icons.Default.Check, null,
                                tint = Color.White, modifier = Modifier.size(14.dp))
                        } else {
                            Text("${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isActive) Color.White
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold)
                        }
                    }
                    /** Connector line **/
                    if (index < steps.lastIndex) {
                        val lineProgress by animateFloatAsState(
                            targetValue = if (index < maxStatus) 1f else 0f,
                            animationSpec = tween(600, easing = FastOutSlowInEasing),
                            label = "line_$index"
                        )
                        Box(
                            Modifier.weight(1f).height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.outline.copy(0.3f))
                        ) {
                            Box(
                                Modifier.fillMaxHeight()
                                    .fillMaxWidth(lineProgress)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
            /** Step labels **/
            Row(Modifier.fillMaxWidth()) {
                steps.forEachIndexed { index, step ->
                    val isActive = index <= maxStatus
                    Text(
                        text = step.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isActive) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}


/** Vendor Order Card **/
@Composable
private fun VendorOrderCard(
    vendorOrder: VendorOrder,
    isCancelling: Boolean,
    onCancelItem: (String) -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Store, null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(vendorOrder.vendorName, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            vendorOrder.items.forEach { item ->
                OrderItemRow(
                    item = item,
                    isCancelling = isCancelling,
                    onCancelItem = { onCancelItem(item.id) },
                )
                if (item != vendorOrder.items.last()) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.15f))
                }
            }
        }
    }
}

@Composable
private fun OrderItemRow(
    item: OrderItem,
    isCancelling: Boolean,
    onCancelItem: () -> Unit,
) {
    val isCancelled = item.status == OrderItemStatus.CANCELLED

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier.alpha(if (isCancelled) 0.5f else 1f),
    ) {
        ProductImage(
            url = item.imageUrl,
            modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.small),
        )
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(item.productName,
                style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
            Text("Qty: ${item.quantity}  •  ${item.lineTotal.formatPrice()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (item.refundAmount > 0) {
                Text("Refunded: ${item.refundAmount.formatPrice()}",
                    style = MaterialTheme.typography.bodySmall, color = OurMallGreen,
                    fontWeight = FontWeight.SemiBold)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                OrderItemStatusChip(item.status)
                if (item.canBeCancelled) {
                    TextButton(
                        onClick = onCancelItem,
                        enabled = !isCancelling,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text("Cancel", color = OurMallRed,
                            style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

/** Financial Summary Card **/
@Composable
private fun FinancialSummaryCard(order: Order) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Financial Summary", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            order.vendorOrders.forEach { vo ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(vo.vendorName, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(vo.activeSubtotal.formatPrice(), style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium)
                }
            }

            if (order.cartLevelDiscountAmount > 0) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Promo (${order.promoCode})", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("-${order.cartLevelDiscountAmount.formatPrice()}",
                        color = OurMallGreen, fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Order Total", fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleSmall)
                val animTotal by animatedDouble(order.grandTotal)
                Text(animTotal.formatPrice(), fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall)
            }

            AnimatedVisibility(visible = order.totalRefunded > 0) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Refunded", style = MaterialTheme.typography.bodyMedium,
                        color = OurMallGreen, fontWeight = FontWeight.SemiBold)
                    Text(order.totalRefunded.formatPrice(), color = OurMallGreen,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

