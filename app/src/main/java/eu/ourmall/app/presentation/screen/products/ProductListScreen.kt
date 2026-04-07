package eu.ourmall.app.presentation.screen.products

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
import eu.ourmall.app.util.StockStatus
import eu.ourmall.app.util.formatPrice
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    /** Infinite scroll trigger **/
    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 3 && state.hasNextPage && !state.isLoadingMore
        }
    }
    LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) viewModel.loadNextPage() }

    /** Add-to-cart snackbar **/
    LaunchedEffect(state.addToCartSuccess) {
        state.addToCartSuccess?.let {
            snackbarHostState.showSnackbar("$it added to cart ✓")
        }
    }

    /** Error snackbar **/
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it, actionLabel = "Retry")
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            OurMallTopBar(
                title = "OurMall",
                cartCount = state.cartItemCount,
                onCartClick = onCartClick,
                onOrdersClick = onOrdersClick,
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp),
                )
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            /** Search Bar **/
            SearchBar(
                query = state.filter.searchQuery,
                onQueryChange = viewModel::onSearchQuery,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            /** Filter Row **/
            FilterRow(
                categories = state.categories,
                selectedCategory = state.filter.category,
                selectedStock = state.filter.stockStatus,
                onCategorySelected = viewModel::onCategorySelected,
                onStockFilterChanged = viewModel::onStockFilterChanged,
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            /** Product List **/
            Box(Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(6) { ProductCardShimmer() }
                        }
                    }
                    !state.isLoading && state.products.isEmpty() && state.error == null -> {
                        EmptyState(
                            icon = Icons.Default.SearchOff,
                            title = "No Products Found",
                            subtitle = "Try adjusting your search or filters",
                            action = {
                                OutlinedButton(onClick = { viewModel.onCategorySelected(null) }) {
                                    Text("Clear Filters")
                                }
                            }
                        )
                    }
                    else -> {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            itemsIndexed(
                                items = state.products,
                                key = { _, p -> p.id }
                            ) { index, product ->
                                AnimatedListItem(index = index) {
                                    ProductCard(
                                        product = product,
                                        onProductClick = { onProductClick(product.id) },
                                        onAddToCart = { viewModel.onAddToCart(product) },
                                    )
                                }
                            }

                            if (state.isLoadingMore) {
                                item {
                                    Box(Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(28.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.primary,
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

/** Search Bar **/
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search products…", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = {
            Icon(Icons.Default.Search, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
}

/** Filter Row **/
@Composable
private fun FilterRow(
    categories: List<String>,
    selectedCategory: String?,
    selectedStock: StockStatus?,
    onCategorySelected: (String?) -> Unit,
    onStockFilterChanged: (StockStatus?) -> Unit,
) {
    val rowScroll = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rowScroll)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        /** All **/
        FilterChip(
            selected = selectedCategory == null && selectedStock == null,
            onClick = { onCategorySelected(null); onStockFilterChanged(null) },
            label = { Text("All") },
            leadingIcon = if (selectedCategory == null && selectedStock == null) {
                { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
            } else null,
        )

        /** Categories **/
        categories.forEach { cat ->
            FilterChip(
                selected = selectedCategory == cat,
                onClick  = { onCategorySelected(if (selectedCategory == cat) null else cat) },
                label    = { Text(cat) },
                leadingIcon = if (selectedCategory == cat) {
                    { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                } else null,
            )
        }

        HorizontalDivider(
            modifier = Modifier.height(24.dp).width(1.dp),
            color = MaterialTheme.colorScheme.outline
        )

        /** Stock filters **/
        listOf(
            StockStatus.IN_STOCK    to "In Stock",
            StockStatus.LOW_STOCK   to "Low Stock",
            StockStatus.OUT_OF_STOCK to "Out of Stock",
        ).forEach { (status, label) ->
            FilterChip(
                selected = selectedStock == status,
                onClick  = { onStockFilterChanged(if (selectedStock == status) null else status) },
                label    = { Text(label) },
                leadingIcon = if (selectedStock == status) {
                    { Icon(Icons.Default.Check, null, Modifier.size(16.dp)) }
                } else null,
            )
        }
    }
}

/** Product Card **/
@Composable
fun ProductCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
) {
    val now = Instant.now()
    val effectivePrice = product.effectivePrice(now)
    val isOfferActive  = product.isOfferActive(now)
    val isOutOfStock   = product.stockStatus == StockStatus.OUT_OF_STOCK

    var addedPulse by remember { mutableStateOf(false) }
    val cardScale by animateFloatAsState(
        targetValue = if (addedPulse) 0.97f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "card_scale"
    )

    Card(
        onClick = onProductClick,
        modifier = Modifier.fillMaxWidth().scale(cardScale),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, hoveredElevation = 4.dp),
    ) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            /** Image **/
            Box(Modifier.size(90.dp)) {
                ProductImage(
                    url = product.imageUrl,
                    modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.small),
                )
                if (isOfferActive && product.discountPercent > 0) {
                    Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(4.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = OurMallOrange,
                    ) {
                        Text(
                            "-${product.discountPercent.toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White, fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                if (isOutOfStock) {
                    Box(
                        Modifier.fillMaxSize()
                            .clip(MaterialTheme.shapes.small)
                            .background(Color.Black.copy(alpha = 0.55f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sold Out",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            /** Content **/
            Column(Modifier.weight(1f)) {
                VendorChip(product.vendorName)
                Spacer(Modifier.height(4.dp))
                Text(
                    product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(6.dp))
                PriceRow(
                    effectivePrice = effectivePrice,
                    originalPrice = product.originalPrice,
                    discountPercent = product.discountPercent,
                    isOfferActive = isOfferActive,
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StockBadge(product.stockStatus)
                    product.offerExpiresAt?.let {
                        if (isOfferActive) CountdownChip(it)
                    }
                }
            }

            /** Add to cart FAB **/
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxHeight().padding(top = 40.dp)
            ) {
                AnimatedVisibility(
                    visible = !isOutOfStock,
                    enter = scaleIn(spring(Spring.DampingRatioLowBouncy)),
                    exit  = scaleOut(),
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            addedPulse = true
                            onAddToCart()
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(Icons.Default.AddShoppingCart, null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
