package eu.ourmall.app.presentation.screen.cart

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.ourmall.app.domain.model.*
import eu.ourmall.app.domain.repository.CartIssueType
import eu.ourmall.app.domain.repository.CartValidationIssue
import eu.ourmall.app.presentation.components.*
import eu.ourmall.app.presentation.theme.*
import eu.ourmall.app.util.StockStatus
import eu.ourmall.app.util.formatPrice
import java.time.Instant

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onCheckout: () -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it); viewModel.clearError() }
    }
    LaunchedEffect(state.promoSuccess) {
        state.promoSuccess?.let { snackbarHostState.showSnackbar(it) }
    }

    /** Validation dialog **/
    if (state.validationIssues.isNotEmpty()) {
        ValidationIssuesDialog(
            issues = state.validationIssues,
            onDismiss = viewModel::dismissValidationIssues,
            onProceed = { viewModel.dismissValidationIssues(); onCheckout() }
        )
    }

    Scaffold(
        topBar = {
            OurMallTopBar(
                title = "My Cart",
                onBack = onBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!state.cart.isEmpty) {
                CartBottomBar(
                    cart = state.cart,
                    isValidating = state.isValidating,
                    onCheckout = { viewModel.onProceedToCheckout(onCheckout) },
                )
            }
        }
    ) { padding ->
        if (state.cart.isEmpty) {
            EmptyState(
                icon = Icons.Default.ShoppingCartCheckout,
                title = "Your Cart is Empty",
                subtitle = "Add products from the store to get started",
                action = {
                    OurMallButton("Browse Products", onClick = onBack,
                        icon = Icons.Default.Storefront)
                }
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding() + 120.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                /** Promo Code Section **/
                item {
                    PromoCodeSection(
                        input    = state.promoInput,
                        onInput  = viewModel::onPromoInputChanged,
                        onApply  = viewModel::onApplyPromo,
                        isLoading = state.isApplyingPromo,
                        error    = state.promoError,
                        appliedCode = state.cart.promoCode,
                    )
                }

                /** Vendor groups **/
                state.cart.vendorCarts.forEach { vendorCart ->
                    item(key = "vendor_${vendorCart.vendorId}") {
                        VendorCartSection(
                            vendorCart = vendorCart,
                            onQuantityChanged = viewModel::onQuantityChanged,
                            onRemoveItem = viewModel::onRemoveItem,
                        )
                    }
                }

                /** Order Summary **/
                item {
                    AnimatedVisibility(visible = true, enter = fadeIn() + expandVertically()) {
                        OrderSummaryCard(cart = state.cart)
                    }
                }
            }
        }
    }
}

/** Vendor Cart Section **/
@Composable
private fun VendorCartSection(
    vendorCart: VendorCart,
    onQuantityChanged: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            /** Vendor header **/
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Store, null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text(vendorCart.vendorName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.weight(1f))
                Text("${vendorCart.itemCount} item${if (vendorCart.itemCount > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            vendorCart.items.forEachIndexed { index, item ->
                CartItemRow(
                    cartItem = item,
                    onQuantityChanged = { onQuantityChanged(item.product.id, it) },
                    onRemove = { onRemoveItem(item.product.id) },
                )
                if (index < vendorCart.items.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.15f))
                }
            }

            /** Vendor subtotal **/
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Vendor Subtotal",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(vendorCart.subtotal.formatPrice(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

/** Cart Item Row **/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onQuantityChanged: (Int) -> Unit,
    onRemove: () -> Unit,
) {
    val now = Instant.now()
    val product = cartItem.product
    val isOfferActive = product.isOfferActive(now)
    val isOutOfStock  = product.stockStatus == StockStatus.OUT_OF_STOCK

    /** Offer expiry inside cart: auto-detect and show warning **/
    val offerExpiredInCart = cartItem.appliedProductDiscount > 0 &&
            product.offerExpiresAt != null &&
            !isOfferActive

    /** Swipe to dismiss **/
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { it == SwipeToDismissBoxValue.EndToStart }
    )
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) onRemove()
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .background(OurMallGreen),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    Modifier.padding(end = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.DeleteOutline, null, tint = Color.White)
                    Text("Remove", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        },
        enableDismissFromStartToEnd = false,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                ProductImage(
                    url = product.imageUrl,
                    modifier = Modifier.size(72.dp).clip(MaterialTheme.shapes.small),
                )
                Column(Modifier.weight(1f).padding(top = 3.dp)) {
                    Text(product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface)

                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(cartItem.snapshotPrice.formatPrice(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface)
                        if (cartItem.appliedProductDiscount > 0 && isOfferActive) {
                            Text(product.originalPrice.formatPrice(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    QuantitySelector(
                        quantity = cartItem.quantity,
                        onDecrease = { onQuantityChanged(cartItem.quantity - 1) },
                        onIncrease = { onQuantityChanged(cartItem.quantity + 1) },
                        maxQuantity = product.stockQuantity,
                    )
                }


                /** Line total **/
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(vertical = 3.dp, horizontal = 3.dp)
                ) {
                    Text(cartItem.discountedLineTotal.formatPrice(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                    if (cartItem.quantity > 1) {
                        Text("×${cartItem.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            /** Offer expiry warning in cart **/
            AnimatedVisibility(visible = offerExpiredInCart) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = OurMallAmber.copy(alpha = 0.12f),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.Warning, null,
                            tint = OurMallAmber, modifier = Modifier.size(14.dp))
                        Text("Offer expired — price updated to ${product.originalPrice.formatPrice()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = OurMallAmber)
                    }
                }
            }

            /** Out of stock warning **/
            AnimatedVisibility(visible = isOutOfStock) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = OurMallRed.copy(alpha = 0.12f),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Default.ErrorOutline, null,
                            tint = OurMallRed, modifier = Modifier.size(14.dp))
                        Text("This item is out of stock",
                            style = MaterialTheme.typography.labelSmall, color = OurMallRed)
                    }
                }
            }

            /** Low stock warning **/
            AnimatedVisibility(visible = product.stockStatus == StockStatus.LOW_STOCK) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = OurMallAmber.copy(alpha = 0.08f),
                ) {
                    Row(Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Inventory2, null,
                            tint = OurMallAmber, modifier = Modifier.size(14.dp))
                        Text("Only ${product.stockQuantity} left!",
                            style = MaterialTheme.typography.labelSmall, color = OurMallAmber)
                    }
                }
            }

            /** Countdown in cart **/
            if (isOfferActive) {
                product.offerExpiresAt?.let { CountdownChip(it) }
            }
        }
    }
}

/** Promo Code Section **/
@Composable
private fun PromoCodeSection(
    input: String,
    onInput: (String) -> Unit,
    onApply: () -> Unit,
    isLoading: Boolean,
    error: String?,
    appliedCode: String?,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.LocalOffer, null,
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Text("Promo Code", style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(visible = appliedCode != null) {
                Surface(shape = RoundedCornerShape(8.dp), color = OurMallGreen.copy(0.12f)) {
                    Row(Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null,
                            tint = OurMallGreen, modifier = Modifier.size(16.dp))
                        Text("\"$appliedCode\" applied!",
                            style = MaterialTheme.typography.bodySmall,
                            color = OurMallGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top) {
                OutlinedTextField(
                    value = input,
                    onValueChange = onInput,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter code (e.g. OURMALL10)") },
                    singleLine = true,
                    isError = error != null,
                    supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
                Button(
                    onClick = onApply,
                    enabled = input.isNotBlank() && !isLoading,
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    if (isLoading) CircularProgressIndicator(
                        Modifier.size(18.dp), strokeWidth = 2.dp, color = Color.White)
                    else Text("Apply", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/** Order Summary Card **/
@Composable
private fun OrderSummaryCard(cart: Cart) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Order Summary", style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold)
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            /** Per-vendor breakdown **/
            cart.vendorCarts.forEach { vc ->
                SummaryRow("${vc.vendorName} (${vc.itemCount} items)",
                    vc.subtotal.formatPrice())
            }

            val totalProductDiscounts = cart.vendorCarts.sumOf { it.totalDiscount }
            if (totalProductDiscounts > 0) {
                SummaryRow("Product Discounts", "-${totalProductDiscounts.formatPrice()}",
                    valueColor = OurMallGreen)
            }

            if (cart.cartLevelDiscountPercent > 0) {
                SummaryRow(
                    "Promo (${cart.promoCode}) -${cart.cartLevelDiscountPercent.toInt()}%",
                    "-${cart.cartLevelDiscountAmount.formatPrice()}",
                    valueColor = OurMallGreen,
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold)

                /** Animated total **/
                val animTotal by animatedDouble(cart.grandTotal)
                Text(animTotal.formatPrice(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String,
                       valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

/** Cart Bottom Bar **/
@Composable
private fun CartBottomBar(
    cart: Cart,
    isValidating: Boolean,
    onCheckout: () -> Unit,
) {
    Surface(shadowElevation = 16.dp, color = MaterialTheme.colorScheme.surface) {
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Total", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                val animTotal by animatedDouble(cart.grandTotal)
                Text(animTotal.formatPrice(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary)
            }
            OurMallButton(
                text = "Checkout (${cart.totalItems})",
                onClick = onCheckout,
                isLoading = isValidating,
                icon = Icons.Default.ArrowForward,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Validation Issues Dialog **/
@Composable
private fun ValidationIssuesDialog(
    issues: List<CartValidationIssue>,
    onDismiss: () -> Unit,
    onProceed: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Warning, null, tint = OurMallAmber) },
        title = { Text("Cart Updated", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Some items in your cart have changed:",
                    style = MaterialTheme.typography.bodyMedium)
                issues.forEach { issue ->
                    val color = when (issue.issueType) {
                        CartIssueType.OUT_OF_STOCK,
                        CartIssueType.INSUFFICIENT_STOCK -> OurMallRed
                        CartIssueType.OFFER_EXPIRED,
                        CartIssueType.PRICE_CHANGED -> OurMallAmber
                    }
                    Surface(shape = RoundedCornerShape(8.dp),
                        color = color.copy(alpha = 0.1f)) {
                        Row(Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                when (issue.issueType) {
                                    CartIssueType.OUT_OF_STOCK,
                                    CartIssueType.INSUFFICIENT_STOCK -> Icons.Default.RemoveShoppingCart
                                    else -> Icons.Default.PriceChange
                                },
                                null, tint = color, modifier = Modifier.size(16.dp)
                            )
                            Text(issue.detail, style = MaterialTheme.typography.bodySmall, color = color)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onProceed) { Text("Proceed Anyway") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Review Cart") }
        },
        shape = MaterialTheme.shapes.large,
    )
}
