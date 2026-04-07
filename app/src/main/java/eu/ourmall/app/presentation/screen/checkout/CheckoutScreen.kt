package eu.ourmall.app.presentation.screen.checkout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.ourmall.app.domain.repository.CartIssueType
import eu.ourmall.app.presentation.components.OurMallButton
import eu.ourmall.app.presentation.components.OurMallTopBar
import eu.ourmall.app.presentation.components.ProductImage
import eu.ourmall.app.presentation.theme.OurMallAmber
import eu.ourmall.app.presentation.theme.OurMallGreen
import eu.ourmall.app.presentation.theme.OurMallRed
import eu.ourmall.app.util.CheckoutStep
import eu.ourmall.app.util.formatPrice

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onOrderPlaced: (String) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it); viewModel.clearError() }
    }

    /** Navigate to order detail when success **/
    LaunchedEffect(state.orderId) {
        if (state.step == CheckoutStep.SUCCESS && state.orderId != null) {
            kotlinx.coroutines.delay(2200)
            onOrderPlaced(state.orderId!!)
        }
    }

    Scaffold(
        topBar = {
            if (state.step != CheckoutStep.SUCCESS) {
                OurMallTopBar(title = "Checkout", onBack = onBack)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (state.step == CheckoutStep.REVIEW) {
                Surface(shadowElevation = 16.dp, color = MaterialTheme.colorScheme.surface) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        /** Warnings from validation **/
                        state.validationIssues.forEach { issue ->
                            val color = if (issue.issueType == CartIssueType.OUT_OF_STOCK)
                                OurMallRed else OurMallAmber
                            Surface(shape = RoundedCornerShape(8.dp),
                                color = color.copy(0.1f)) {
                                Row(Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, null,
                                        tint = color, modifier = Modifier.size(14.dp))
                                    Text(issue.detail,
                                        style = MaterialTheme.typography.labelSmall, color = color)
                                }
                            }
                        }
                        OurMallButton(
                            text = "Place Order  •  ${state.cart.grandTotal.formatPrice()}",
                            onClick = viewModel::placeOrder,
                            isLoading = state.isValidating || state.isPlacingOrder,
                            icon = Icons.Default.Lock,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.cart.isEmpty &&
                                    state.validationIssues.none {
                                        it.issueType == CartIssueType.OUT_OF_STOCK
                                    },
                        )
                    }
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                fadeIn(tween(500)) + scaleIn(tween(500), initialScale = 0.92f) togetherWith
                fadeOut(tween(300))
            },
            label = "checkout_step",
        ) { step ->
            when (step) {
                CheckoutStep.REVIEW -> ReviewContent(state = state, padding = padding)
                CheckoutStep.PLACING -> PlacingOrderContent()
                CheckoutStep.SUCCESS -> OrderSuccessContent(orderId = state.orderId ?: "")
            }
        }
    }
}

/** Review Content **/
@Composable
private fun ReviewContent(
    state: CheckoutUiState,
    padding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        /** Section header **/
        Text("Review Order", style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold)

        /** Vendor groups **/
        state.cart.vendorCarts.forEach { vendorCart ->
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Store, null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp))
                        Text(vendorCart.vendorName,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleSmall)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

                    vendorCart.items.forEach { item ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            ProductImage(
                                url = item.product.imageUrl,
                                modifier = Modifier.size(52.dp)
                                    .clip(MaterialTheme.shapes.small),
                            )
                            Column(Modifier.weight(1f)) {
                                Text(item.product.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                Text("Qty: ${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(item.discountedLineTotal.formatPrice(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface)
                        }
                    }

                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(vendorCart.subtotal.formatPrice(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        /** Totals card **/
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.cart.cartLevelDiscountPercent > 0) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Promo Discount", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("-${state.cart.cartLevelDiscountAmount.formatPrice()}",
                            color = OurMallGreen, fontWeight = FontWeight.Bold)
                    }
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Grand Total", style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(state.cart.grandTotal.formatPrice(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

/** Placing Order Animation **/
@Composable
private fun PlacingOrderContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)) {
            val inf = rememberInfiniteTransition(label = "placing")
            val rotation by inf.animateFloat(
                0f, 360f,
                infiniteRepeatable(tween(1200, easing = LinearEasing)),
                label = "placing_rotate"
            )
            Box(
                Modifier.size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.ShoppingBag, null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp).graphicsLayer { rotationZ = rotation })
            }
            Text("Placing your order…",
                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Please wait a moment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            LinearProgressIndicator(
                modifier = Modifier.width(200.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/** Order Success Animation **/
@Composable
private fun OrderSuccessContent(orderId: String) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp),
        ) {
            /** Bouncing check circle **/
            val scale by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
                label = "success_scale"
            )

            Box(
                Modifier
                    .size(120.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(OurMallGreen.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.CheckCircle, null,
                    tint = OurMallGreen,
                    modifier = Modifier.size(72.dp),
                )
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(600, 300)) + slideInVertically(
                    tween(600, 300), initialOffsetY = { it / 2 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Order Placed!", style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold, color = OurMallGreen)
                    Text(orderId, style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Taking you to your order details…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}
