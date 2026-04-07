package eu.ourmall.app.presentation.screen.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.ourmall.app.presentation.components.CountdownChip
import eu.ourmall.app.presentation.components.EmptyState
import eu.ourmall.app.presentation.components.OurMallButton
import eu.ourmall.app.presentation.components.OurMallTopBar
import eu.ourmall.app.presentation.components.PriceRow
import eu.ourmall.app.presentation.components.ProductImage
import eu.ourmall.app.presentation.components.QuantitySelector
import eu.ourmall.app.presentation.components.StockBadge
import eu.ourmall.app.presentation.components.VendorChip
import eu.ourmall.app.presentation.theme.OurMallGreen
import eu.ourmall.app.presentation.theme.OurMallOrange
import eu.ourmall.app.util.StockStatus
import eu.ourmall.app.util.formatPrice
import java.time.Instant

@Composable
fun ProductDetailScreen(
    productId: String,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            OurMallTopBar(
                title = "Product Detail",
                onBack = onBack,
                cartCount = state.cartItemCount,
                onCartClick = onCartClick,
            )
        },
        bottomBar = {
            state.product?.let { product ->
                val isOutOfStock = product.stockStatus == StockStatus.OUT_OF_STOCK
                Surface(
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        QuantitySelector(
                            quantity = state.quantity,
                            onDecrease = viewModel::decreaseQty,
                            onIncrease = viewModel::increaseQty,
                            maxQuantity = product.stockQuantity,
                        )
                        OurMallButton(
                            text = if (isOutOfStock) "Out of Stock"
                                   else if (state.addedToCartSuccess) "Added to Cart ✓"
                                   else "Add to Cart",
                            onClick = viewModel::addToCart,
                            enabled = !isOutOfStock,
                            isLoading = state.isAddingToCart,
                            icon = if (!state.addedToCartSuccess) Icons.Default.ShoppingCart else null,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            state.error != null -> {
                EmptyState(
                    icon = Icons.Default.ErrorOutline,
                    title = "Failed to Load",
                    subtitle = state.error ?: "Something went wrong",
                    action = { OutlinedButton(onClick = onBack) { Text("Go Back") } }
                )
            }
            state.product != null -> {
                val product = state.product!!
                val now = Instant.now()
                val effectivePrice = product.effectivePrice(now)
                val isOfferActive  = product.isOfferActive(now)

                /** Animate content entry **/
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState()),
                ) {
                    /** Hero Image **/
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400)) + expandVertically(tween(400))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            ProductImage(
                                url = product.imageUrl,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                            )
                            /** Gradient overlay **/
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                                            )
                                        )
                                    )
                            )
                            if (isOfferActive) {
                                Surface(
                                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = OurMallOrange,
                                ) {
                                    Text(
                                        "-${product.discountPercent.toInt()}% OFF",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.White, fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    /** Product Info **/
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(400, 150)) + slideInVertically(
                            tween(400, 150), initialOffsetY = { it / 4 }
                        )
                    ) {
                        Column(Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)) {

                            VendorChip(product.vendorName)

                            Text(
                                product.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                PriceRow(
                                    effectivePrice = effectivePrice,
                                    originalPrice = product.originalPrice,
                                    discountPercent = product.discountPercent,
                                    isOfferActive = isOfferActive,
                                )
                                StockBadge(product.stockStatus)
                            }

                            /** Countdown **/
                            if (isOfferActive) {
                                product.offerExpiresAt?.let { expiry ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = OurMallOrange.copy(alpha = 0.08f),
                                        border = BorderStroke(1.dp, OurMallOrange.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            Modifier.fillMaxWidth().padding(14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Column {
                                                Text("Flash Sale", fontWeight = FontWeight.Bold,
                                                    color = OurMallOrange,
                                                    style = MaterialTheme.typography.bodyMedium)
                                                Text("Offer expires soon!",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            CountdownChip(expiry)
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(0.3f))

                            /** Details section **/
                            DetailRow("Category", product.category)
                            DetailRow("Vendor", product.vendorName)
                            DetailRow("Stock Qty", "${product.stockQuantity} units")

                            /** Savings callout **/
                            if (isOfferActive && product.discountPercent > 0) {
                                val savings = product.originalPrice - effectivePrice
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = OurMallGreen.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, OurMallGreen.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth().padding(14.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(Icons.Default.LocalOffer, null,
                                            tint = OurMallGreen, modifier = Modifier.size(20.dp))
                                        Text(
                                            "You save ${savings.formatPrice()} with this offer!",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = OurMallGreen, fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface)
    }
}
