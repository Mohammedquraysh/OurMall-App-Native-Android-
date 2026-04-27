OurMall Android App
Company: FXCareer.eu
Role: Mobile Application Developer Assessment
Stack: Kotlin · Jetpack Compose · Clean Architecture · MVVM

What is this?
A fully offline-capable Android shopping app built as a developer assessment. It simulates a real multi-vendor marketplace — product browsing, cart management, promo codes, checkout, and order tracking — with no live backend required. Everything runs through a self-contained mock API that simulates real network latency, occasional failures, and stock fluctuations.

Getting Started
Requirements

Android Studio Hedgehog (2023.1.1) or newer
JDK 17
Android SDK 35
Android 8.0+ device or emulator (API 26+)

Steps

Clone the repository
Open in Android Studio → File → Open
Wait for Gradle sync
Hit Shift + F10 to run


No API keys needed. The app is fully self-contained.


Features
🛍️ Product Listing

Product cards with image, name, price, vendor, discount badge and stock status
Live countdown timer per offer — price auto-reverts when the offer expires
Search with debounce, category filters, and stock status filters
Pagination — loads the next page as you scroll toward the bottom
Shimmer placeholders while loading
Error handling with retry on API failure

🛒 Cart

Add products from multiple vendors into one cart
Re-adding the same product increments quantity instead of duplicating
Discounts are snapshotted at add time
Promo code support (see codes below)
Warning banner if an offer expires while items are in the cart
Stock validation with low-stock warnings
Swipe to delete items
Animated quantity selector
Per-vendor subtotal breakdown + animated grand total
Prices auto-refresh every 30 seconds while the cart is open

✅ Checkout & Orders

Pre-checkout validation — catches stale prices, expired offers, and stock issues before you proceed
Orders grouped by vendor automatically
Animated placing → success screen
Cancel a full order or a single item independently
Totals and refund amounts recalculate instantly after cancellation
Order item status flow: Pending → Confirmed → Shipped → Delivered / Cancelled
Animated status stepper on the order detail screen


Promo Codes
CodeDiscountOURMALL1010% offSAVE2020% offWELCOME55% off

Animations
WhereWhatScreen transitionsSlide in/out with fadeProduct detailHero image expand + content slides upProduct cardsStaggered entrance on list loadAdd to cart buttonBounce spring on tapCart badgeScale-in spring when count changesCountdown chipRed pulse when under 10 minutesStock badgePulsing green dotQuantity selectorSlide up/down number transitionCart & order totalsAnimated count-up on price changeShimmer loadersSweeping shimmer on all placeholdersCheckout screenRotating bag icon + progress barOrder successBouncing check circleStatus stepperProgress line fill animation

Architecture
This project follows Clean Architecture with three distinct layers that have a strict one-way dependency rule.
Presentation → Domain ← Data
Domain sits in the middle and knows nothing about the other two layers. It contains only pure Kotlin — business models, repository interfaces, and use cases. Data implements those interfaces using Room and the mock API. Presentation holds the ViewModels and Compose screens, observing state via StateFlow.
app/src/main/java/eu/ourmall/app/
│
├── data/
│   ├── local/
│   │   ├── dao/          CartDao, OrderDao, PromoDao
│   │   ├── database/     OurMallDatabase
│   │   └── entity/       CartItemEntity, OrderEntity
│   ├── remote/
│   │   ├── api/          MockProductApi
│   │   └── dto/          DTOs + ApiResponse wrapper
│   └── repository/       Implementations + Mappers
│
├── domain/
│   ├── model/            Product, Cart, Order, …
│   ├── repository/       Repository interfaces
│   └── usecase/          GetProducts, AddToCart, …
│
├── presentation/
│   ├── theme/            Material 3 theme + colors
│   ├── navigation/       NavGraph with animated transitions
│   ├── components/       Shared UI components
│   └── screen/
│       ├── products/     ProductListScreen, ProductDetailScreen
│       ├── cart/         CartScreen
│       ├── checkout/     CheckoutScreen
│       └── order/        OrderListScreen, OrderDetailScreen
│
├── di/                   Hilt modules
└── util/                 Extension functions

Libraries
LibraryPurposeJetpack Compose BOM 2024.08Declarative UIHilt 2.51Dependency injectionRoom 2.6Local persistence — cart and ordersNavigation ComposeScreen routing with animated transitionsCoil 2.7Async image loadingCoroutines + FlowAsync operations and reactive stateGsonJSON serialisation for order storage

Notes

Prices are in Nigerian Naira (₦), reflecting OurMall.eu's Nigerian market.
The mock API introduces 400–900ms simulated latency, a 5% random failure rate, and random stock fluctuations to mimic real-world conditions.
Order statuses are seeded as PENDING at creation. In a production app a background worker or push notification would advance these through the status flow.
Refund logic is scoped to the exact line total of each cancelled item. Promo discounts are not redistributed — this is a deliberate simplification.
Cart and order data persist across app restarts via Room.
