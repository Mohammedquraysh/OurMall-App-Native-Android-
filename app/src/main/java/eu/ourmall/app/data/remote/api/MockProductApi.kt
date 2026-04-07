package eu.ourmall.app.data.remote.api

import eu.ourmall.app.data.remote.dto.*
import kotlinx.coroutines.delay
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Mock API that simulates a real backend with:
 * - Network delay
 * - Occasional failures (5% chance)
 * - Dynamic stock changes
 * - Offer expiry
 */
@Singleton
class MockProductApi @Inject constructor() {

    private val vendors = listOf(
        VendorDto("v1", "TechZone Electronics"),
        VendorDto("v2", "Fashion Forward"),
        VendorDto("v3", "Home & Living"),
        VendorDto("v4", "Sports Galaxy"),
        VendorDto("v5", "Beauty Luxe"),
    )

    private val categories = listOf("Electronics", "Fashion", "Home", "Sports", "Beauty", "Books")

    private val stockMap = mutableMapOf<String, Int>()

    private val allProducts: List<ProductDto> by lazy {
        buildList {

            /** Electronics - TechZone **/
            add(product("p001", "Samsung 4K QLED TV 55\"", "https://picsum.photos/seed/tv55/400/400",
                450_000.0, 15.0, hoursFromNow(2), "v1", "Electronics", 12))
            add(product("p002", "Apple AirPods Pro (2nd Gen)", "https://picsum.photos/seed/airpods/400/400",
                185_000.0, 10.0, hoursFromNow(6), "v1", "Electronics", 25))
            add(product("p003", "Sony WH-1000XM5 Headphones", "https://picsum.photos/seed/sony/400/400",
                120_000.0, 20.0, hoursFromNow(1), "v1", "Electronics", 3))
            add(product("p004", "iPad Air 5th Generation", "https://picsum.photos/seed/ipad/400/400",
                350_000.0, 0.0, null, "v1", "Electronics", 8))
            add(product("p005", "Samsung Galaxy S24 Ultra", "https://picsum.photos/seed/s24/400/400",
                680_000.0, 5.0, hoursFromNow(12), "v1", "Electronics", 15))
            add(product("p006", "Dell XPS 15 Laptop", "https://picsum.photos/seed/dell/400/400",
                920_000.0, 8.0, hoursFromNow(3), "v1", "Electronics", 0))

            /** Fashion - Fashion Forward **/
            add(product("p007", "Nike Air Max 270", "https://picsum.photos/seed/nike/400/400",
                45_000.0, 25.0, hoursFromNow(4), "v2", "Fashion", 20))
            add(product("p008", "Levi's 501 Original Jeans", "https://picsum.photos/seed/levis/400/400",
                18_500.0, 0.0, null, "v2", "Fashion", 35))
            add(product("p009", "Ray-Ban Aviator Sunglasses", "https://picsum.photos/seed/rayban/400/400",
                32_000.0, 12.0, hoursFromNow(8), "v2", "Fashion", 2))
            add(product("p010", "Zara Trench Coat", "https://picsum.photos/seed/zara/400/400",
                28_000.0, 30.0, hoursFromNow(1), "v2", "Fashion", 7))

            /** Home & Living **/
            add(product("p011", "Nespresso Vertuo Coffee Machine", "https://picsum.photos/seed/nespresso/400/400",
                65_000.0, 18.0, hoursFromNow(5), "v3", "Home", 10))
            add(product("p012", "Dyson V15 Vacuum Cleaner", "https://picsum.photos/seed/dyson/400/400",
                145_000.0, 0.0, null, "v3", "Home", 4))
            add(product("p013", "KitchenAid Stand Mixer", "https://picsum.photos/seed/kitchenaid/400/400",
                89_000.0, 22.0, hoursFromNow(3), "v3", "Home", 6))
            add(product("p014", "IKEA KALLAX Shelf Unit", "https://picsum.photos/seed/kallax/400/400",
                25_000.0, 0.0, null, "v3", "Home", 0))


            /** Sports **/
            add(product("p015", "Garmin Forerunner 265 GPS Watch", "https://picsum.photos/seed/garmin/400/400",
                210_000.0, 15.0, hoursFromNow(7), "v4", "Sports", 9))
            add(product("p016", "Wilson Pro Staff Tennis Racket", "https://picsum.photos/seed/wilson/400/400",
                38_000.0, 0.0, null, "v4", "Sports", 14))
            add(product("p017", "Adidas Ultraboost 23 Running Shoes", "https://picsum.photos/seed/adidas/400/400",
                55_000.0, 20.0, hoursFromNow(2), "v4", "Sports", 18))


            /** Beauty **/
            add(product("p018", "La Mer Moisturizing Cream 60ml", "https://picsum.photos/seed/lamer/400/400",
                78_000.0, 10.0, hoursFromNow(9), "v5", "Beauty", 5))
            add(product("p019", "Charlotte Tilbury Pillow Talk Palette", "https://picsum.photos/seed/ct/400/400",
                22_000.0, 0.0, null, "v5", "Beauty", 22))
            add(product("p020", "Dyson Airwrap Multi-Styler", "https://picsum.photos/seed/airwrap/400/400",
                165_000.0, 12.0, hoursFromNow(6), "v5", "Beauty", 3))
        }.also { list ->
            list.forEach { stockMap[it.id] = it.stockQuantity }
        }
    }



    /** Public API **/
    suspend fun getProducts(
        query: String = "",
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        stockFilter: String? = null,
        page: Int = 0,
        pageSize: Int = 10
    ): ApiResponse<PagedResponse<ProductDto>> {
        simulateNetwork()
        maybeThrowError()
        /**  Simulate live stock updates **/
        randomlyUpdateStock()

        val filtered = allProducts
            .filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
            .filter { category == null || it.category.equals(category, ignoreCase = true) }
            .filter { minPrice == null || it.originalPrice >= minPrice }
            .filter { maxPrice == null || it.originalPrice <= maxPrice }
            .filter {
                when (stockFilter) {
                    "IN_STOCK" -> stockMap.getOrDefault(it.id, 0) > 5
                    "LOW_STOCK" -> stockMap.getOrDefault(it.id, 0) in 1..5
                    "OUT_OF_STOCK" -> stockMap.getOrDefault(it.id, 0) == 0
                    else -> true
                }
            }
            .map { it.copy(stockQuantity = stockMap.getOrDefault(it.id, 0)) }

        val totalCount = filtered.size
        val start = page * pageSize
        val paged = filtered.drop(start).take(pageSize)

        return ApiResponse.Success(
            PagedResponse(
                items = paged,
                totalCount = totalCount,
                page = page,
                pageSize = pageSize,
                hasNextPage = start + pageSize < totalCount
            )
        )
    }

    suspend fun getProductById(id: String): ApiResponse<ProductDto> {
        simulateNetwork()
        maybeThrowError()
        val product = allProducts.find { it.id == id }
            ?: return ApiResponse.Error(404, "Product not found")
        return ApiResponse.Success(product.copy(stockQuantity = stockMap.getOrDefault(id, 0)))
    }

    suspend fun getCategories(): ApiResponse<List<String>> {
        simulateNetwork(short = true)
        return ApiResponse.Success(categories)
    }

    suspend fun validateCartItems(productIds: List<String>): ApiResponse<List<ProductDto>> {
        simulateNetwork()
        val products = productIds.mapNotNull { id ->
            allProducts.find { it.id == id }
                ?.copy(stockQuantity = stockMap.getOrDefault(id, 0))
        }
        return ApiResponse.Success(products)
    }

    suspend fun validatePromoCode(code: String): ApiResponse<PromoCodeDto> {
        simulateNetwork(short = true)
        return when (code.uppercase()) {
            "OURMALL10" -> ApiResponse.Success(PromoCodeDto(code, 10.0, "10% off your order"))
            "SAVE20" -> ApiResponse.Success(PromoCodeDto(code, 20.0, "20% off your order"))
            "WELCOME5" -> ApiResponse.Success(PromoCodeDto(code, 5.0, "Welcome discount 5%"))
            else -> ApiResponse.Error(404, "Invalid promo code")
        }
    }

    /** Helpers **/
    private suspend fun simulateNetwork(short: Boolean = false) {
        delay(if (short) Random.nextLong(100, 300) else Random.nextLong(400, 900))
    }

    private fun maybeThrowError() {
        if (Random.nextFloat() < 0.05f) throw Exception("Network error: Please check your connection")
    }

    private fun randomlyUpdateStock() {
        if (Random.nextFloat() < 0.1f) {
            val randomId = allProducts.random().id
            val current = stockMap.getOrDefault(randomId, 0)
            stockMap[randomId] = maxOf(0, current + Random.nextInt(-2, 3))
        }
    }

    private fun product(
        id: String, name: String, imageUrl: String,
        price: Double, discountPercent: Double, offerExpiresAt: Instant?,
        vendorId: String, category: String, stock: Int,
    ): ProductDto {
        val vendor = vendors.first { it.id == vendorId }
        return ProductDto(
            id = id,
            name = name,
            imageUrl = imageUrl,
            originalPrice = price,
            discountPercent = discountPercent,
            offerExpiresAtEpoch = offerExpiresAt?.epochSecond,
            vendorId = vendorId,
            vendorName = vendor.name,
            category = category,
            stockQuantity = stock,
        )
    }


    private fun hoursFromNow(hours: Long): Instant =
        Instant.now().plusSeconds(hours * 3600)
}
