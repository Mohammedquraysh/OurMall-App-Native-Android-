package eu.ourmall.app.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import eu.ourmall.app.presentation.theme.OurMallAmber
import eu.ourmall.app.presentation.theme.OurMallBlue
import eu.ourmall.app.presentation.theme.OurMallGreen
import eu.ourmall.app.presentation.theme.OurMallOrange
import eu.ourmall.app.presentation.theme.OurMallRed
import eu.ourmall.app.presentation.theme.PulsingDot
import eu.ourmall.app.presentation.theme.shimmerBrush
import eu.ourmall.app.util.OrderItemStatus
import eu.ourmall.app.util.StockStatus
import eu.ourmall.app.util.formatCountdown
import eu.ourmall.app.util.formatPrice
import java.time.Instant

/** Shimmer Placeholder **/
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(shimmerBrush())
    )
}

@Composable
fun ProductCardShimmer() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShimmerBox(Modifier.size(90.dp).clip(MaterialTheme.shapes.small))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerBox(Modifier.fillMaxWidth(0.7f).height(16.dp))
                ShimmerBox(Modifier.fillMaxWidth(0.4f).height(14.dp))
                ShimmerBox(Modifier.fillMaxWidth(0.5f).height(20.dp))
                ShimmerBox(Modifier.fillMaxWidth(0.6f).height(12.dp))
            }
        }
    }
}

/** Stock Badge **/
@Composable
fun StockBadge(status: StockStatus) {
    val (text, color) = when (status) {
        StockStatus.IN_STOCK    -> "In Stock"    to OurMallGreen
        StockStatus.LOW_STOCK   -> "Low Stock"   to OurMallAmber
        StockStatus.OUT_OF_STOCK-> "Out of Stock" to OurMallRed
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (status == StockStatus.IN_STOCK) {
            PulsingDot(color)
        } else {
            Box(Modifier.size(7.dp).clip(CircleShape).background(color))
        }
        Text(text, style = MaterialTheme.typography.labelSmall,
            color = color, fontWeight = FontWeight.SemiBold)
    }
}

/** Countdown Timer **/
@Composable
fun CountdownChip(expiresAt: Instant) {
    var secondsLeft by remember { mutableLongStateOf(
        maxOf(0L, expiresAt.epochSecond - Instant.now().epochSecond)
    )}

    LaunchedEffect(expiresAt) {
        while (secondsLeft > 0) {
            kotlinx.coroutines.delay(1000)
            secondsLeft = maxOf(0L, expiresAt.epochSecond - Instant.now().epochSecond)
        }
    }

    /** Pulse red when < 10 minutes **/
    val isUrgent = secondsLeft in 1..600
    val inf = rememberInfiniteTransition(label = "countdown_pulse")
    val alpha by inf.animateFloat(
        initialValue = if (isUrgent) 0.7f else 1f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "countdown_alpha"
    )

    if (secondsLeft <= 0) return

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isUrgent) OurMallRed.copy(alpha = 0.15f) else OurMallOrange.copy(alpha = 0.12f),
        modifier = Modifier.graphicsLayer { this.alpha = if (isUrgent) alpha else 1f }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.Default.Timer,
                contentDescription = null,
                tint = if (isUrgent) OurMallRed else OurMallOrange,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "Ends in ${secondsLeft.formatCountdown()}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isUrgent) OurMallRed else OurMallOrange,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/** Price Row **/
@Composable
fun PriceRow(
    effectivePrice: Double,
    originalPrice: Double,
    discountPercent: Double,
    isOfferActive: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = effectivePrice.formatPrice(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (isOfferActive && discountPercent > 0) {
            Text(
                text = originalPrice.formatPrice(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
            )
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = OurMallOrange,
            ) {
                Text(
                    text = "-${discountPercent.toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/** Vendor Chip **/
@Composable
fun VendorChip(vendorName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            Icons.Default.Store,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = vendorName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Product Image **/
@Composable
fun ProductImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    var isLoading by remember { mutableStateOf(true) }
    Box(modifier) {
        if (isLoading) {
            ShimmerBox(Modifier.matchParentSize())
        }
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
            onSuccess = { isLoading = false },
            onError   = { isLoading = false },
        )
    }
}

/** OurMall Top Bar **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OurMallTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    cartCount: Int = 0,
    onCartClick: (() -> Unit)? = null,
    onOrdersClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBackIosNew, null,
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        },
        actions = {
            actions()
            if (onOrdersClick != null) {
                IconButton(onClick = onOrdersClick) {
                    Icon(Icons.Default.Receipt, null,
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            if (onCartClick != null) {
                BadgedBox(badge = {
                    /**
                     * Use explicit androidx.compose.animation.AnimatedVisibility to avoid
                     the RowScope receiver conflict inside the BoxScope badge lambda
                     **/
                    androidx.compose.animation.AnimatedVisibility(
                        visible = cartCount > 0,
                        enter = scaleIn(spring(Spring.DampingRatioLowBouncy)),
                        exit  = scaleOut(),
                    ) {
                        Badge { Text("$cartCount") }
                    }
                }) {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, null,
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    )
}

/** Quantity Selector **/
@Composable
fun QuantitySelector(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    maxQuantity: Int = 99,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "qty_scale"
    )
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onDecrease,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                if (quantity <= 1) Icons.Default.DeleteOutline else Icons.Default.Remove,
                null,
                tint = if (quantity <= 1) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(16.dp)
            )
        }
        AnimatedContent(
            targetState = quantity,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } + fadeIn() togetherWith
                            slideOutVertically { it } + fadeOut()
                } else {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                }
            },
            label = "qty_anim"
        ) { qty ->
            Text(
                text = "$qty",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.widthIn(min = 28.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        IconButton(
            onClick = onIncrease,
            enabled = quantity < maxQuantity,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(Icons.Default.Add, null,
                tint = if (quantity < maxQuantity) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp))
        }
    }
}


/** Empty state **/
@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    action: (@Composable () -> Unit)? = null,
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(Spring.DampingRatioLowBouncy, stiffness = 200f),
        label = "empty_scale"
    )
    Column(
        Modifier.fillMaxSize().scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(44.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp))
        if (action != null) {
            Spacer(Modifier.height(24.dp))
            action()
        }
    }
}


/** Primary Button **/
@Composable
fun OurMallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(0.12f),
        ),
    ) {
        AnimatedContent(
            targetState = isLoading,
            label = "btn_content"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (icon != null) Icon(icon, null, modifier = Modifier.size(18.dp))
                    Text(text, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

/** Order Status Chip **/
@Composable
fun OrderItemStatusChip(status: OrderItemStatus) {
    val (text, color) = when (status) {
        OrderItemStatus.PENDING   -> "Pending"   to OurMallAmber
        OrderItemStatus.CONFIRMED -> "Confirmed" to OurMallBlue
        OrderItemStatus.SHIPPED   -> "Shipped"   to OurMallOrange
        OrderItemStatus.DELIVERED -> "Delivered" to OurMallGreen
        OrderItemStatus.CANCELLED -> "Cancelled" to OurMallRed
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}