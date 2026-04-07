# OurMall Android App

**Company:** OurMall.eu  
**Role:** Mobile Application Developer Assessment  
**Framework:** Android — Jetpack Compose + Clean Architecture + MVVM  
**Language:** Kotlin

---

## Architecture

```
app/
└── src/main/java/eu/ourmall/app/
    ├── data/
    │   ├── local/
    │   │   ├── dao/          Room DAOs (CartDao, OrderDao, PromoDao)
    │   │   ├── database/     OurMallDatabase (Room)
    │   │   └── entity/       DB entities (CartItemEntity, OrderEntity)
    │   ├── remote/
    │   │   ├── api/          MockProductApi — simulates real API with delays/errors
    │   │   └── dto/          Data Transfer Objects + ApiResponse wrapper
    │   └── repository/       Repository implementations + Mappers
    ├── domain/
    │   ├── model/            Pure Kotlin domain models (Product, Cart, Order…)
    │   ├── repository/       Repository interfaces
    │   └── usecase/          One use case per action (GetProducts, AddToCart…)
    ├── presentation/
    │   ├── theme/            Material3 theme, colors, animations
    │   ├── navigation/       NavGraph with animated transitions
    │   ├── components/       Reusable UI components
    │   └── screen/
    │       ├── products/     ProductListScreen + ProductDetailScreen
    │       ├── cart/         CartScreen
    │       ├── checkout/     CheckoutScreen
    │       └── order/        OrderListScreen + OrderDetailScreen
    ├── di/                   Hilt DI module
    └── util/                 Extension functions
```

### Clean Architecture Layers
| Layer | Responsibility |
|---|---|
| **Domain** | Pure business rules. No Android dependencies. |
| **Data** | API + Room implementations. Maps DTOs/entities → domain models. |
| **Presentation** | ViewModels + Compose UI. Observes StateFlow, dispatches actions. |

### Dependency Rule
```
Presentation → Domain ← Data
```
Domain knows nothing about Data or Presentation. Data depends only on Domain interfaces.

---

## Libraries Used

| Library | Purpose |
|---|---|
| Jetpack Compose BOM 2024.08 | Declarative UI |
| Hilt 2.51 | Dependency injection |
| Room 2.6 | Local persistence (cart, orders) |
| Navigation Compose | Screen routing with animated transitions |
| Coil 2.7 | Async image loading |
| Coroutines + Flow | Async streams, reactive state |
| Gson | JSON serialisation for order storage |

---

## How to Run

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35
- Minimum device/emulator: Android 8.0 (API 26)

### Steps
1. Clone the project
2. Open in Android Studio (`File → Open`)
3. Wait for Gradle sync to complete
4. Run on a device or emulator (`Shift+F10`)

> No API keys needed — the app uses a fully self-contained mock API.

---

## Features Implemented

### Task 1 — Product Listing + Dynamic Pricing
- [x] Product cards: image, name, price, vendor, discount badge, stock status
- [x] Live countdown timer per offer (auto-updates every second)
- [x] **Offer expiry**: price auto-reverts when countdown hits zero
- [x] Search with 400ms debounce
- [x] Filters: category chips, stock status chips
- [x] Pagination / lazy loading (loads next page when 3 items from bottom)
- [x] API failure handling with error snackbar + retry
- [x] Shimmer loading placeholders

### Task 2 — Cart + Multi-Vendor Logic
- [x] Add from multiple vendors into a single cart
- [x] **Duplicate merging**: re-adding same product increments quantity
- [x] Product-level discounts (snapshotted at add time)
- [x] Cart-level discounts via promo codes (`OURMALL10`, `SAVE20`, `WELCOME5`)
- [x] **Offer expiry inside cart**: warning banner + price update shown
- [x] Stock validation + low-stock warning in cart
- [x] Swipe-to-delete cart items
- [x] Animated quantity selector with count-up/down transitions
- [x] Per-vendor subtotal breakdown + grand total with animated count-up
- [x] Auto price refresh every 30 seconds while cart is open

### Task 3 — Checkout + Order + Cancellation
- [x] Pre-checkout validation: stock, price freshness, offer expiry
- [x] Validation issues shown before blocking checkout
- [x] Order creation with **vendor-wise grouping** (one VendorOrder per vendor)
- [x] Animated placing order screen → animated success screen
- [x] Cancel **full order** (all cancellable items)
- [x] Cancel **single item** (item-level, others unaffected)
- [x] Recalculated totals after any cancellation
- [x] Refund amount scoped to cancelled items only
- [x] Item status flow: Pending → Confirmed → Shipped → Delivered / Cancelled
- [x] Animated status stepper on order detail

### System Design
- [x] See `SYSTEM_DESIGN.md` — multi-vendor order structure, split logic, cancellation, API design

---

## Animations Summary

| Location | Animation |
|---|---|
| Screen transitions | Slide in/out + fade (NavGraph) |
| Product detail entry | Hero image expand + content slide up |
| Product cards | Staggered list entrance (delayed per index) |
| Add to cart button | Bounce scale spring |
| Cart badge | Scale-in spring on count change |
| Countdown chip | Red pulse when < 10 minutes remaining |
| Stock badge | Pulsing green dot for in-stock |
| Quantity selector | Slide up/down number transition |
| Cart total | Animated count-up on price change |
| Order total | Animated count-up on cancellation |
| Shimmer loaders | Sweeping shimmer on all placeholders |
| Checkout placing | Rotating bag icon + linear progress |
| Order success | Bouncing check circle spring animation |
| Status stepper | Progress line fill animation |
| Cart validation | AnimatedVisibility for warning banners |
| Empty states | Scale-in spring on appearance |
| Offer expiry warning | AnimatedVisibility slide in |

---

## Mock Promo Codes

| Code | Discount |
|---|---|
| `OURMALL10` | 10% off |
| `SAVE20` | 20% off |
| `WELCOME5` | 5% off |

---

## Notes & Assumptions

- **No live backend**: all data comes from `MockProductApi` which simulates network latency (400–900ms), occasional 5% failure rate, and random stock fluctuations.
- **Prices in Nigerian Naira (₦)**: aligned with OurMall.eu's Nigerian market focus.
- **Order item statuses** are seeded as `PENDING` at creation. In a real app a background worker would poll/receive webhooks to advance statuses.
- **Refund policy**: each cancelled item refunds exactly its `lineTotal`. Cart-level (promo) discounts are not redistributed — this is intentional for simplicity.
- The cart persists across app restarts via Room. Orders also persist locally.
