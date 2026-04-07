# OurMall-App-Native-Android-

**Company:** OurMall.eu  
**Role:** Mobile Application Developer Assessment  
**Framework:** Android — Jetpack Compose + Clean Architecture + MVVM  
**Language:** Kotlin




## Architecture

```
The Android app follows a clean architecture approach, structured into data, domain, and presentation layers for scalability and maintainability.

Data Layer

Local: Uses Room for local storage, including DAOs (CartDao, OrderDao, PromoDao), the database setup (OurMallDatabase), and entities such as CartItemEntity and OrderEntity.

Remote: Contains API-related logic, including a mock product API that simulates real network behavior (delays and errors), along with DTOs and an ApiResponse wrapper.

Repository: Implements data operations and handles mapping between data and domain models.

Domain Layer

Model: Contains pure Kotlin models such as Product, Cart, and Order.

Repository: Defines interfaces for data operations.

Use Case: Encapsulates business logic, with one use case per action (e.g., GetProducts, AddToCart).

Presentation Layer

Theme: Handles Material 3 theming, colors, and animations.

Navigation: Manages navigation using a NavGraph with animated transitions.

Components: Contains reusable UI components.

Screens:

Products: Product list and product detail screens.

Cart: Cart screen.

Checkout: Checkout screen.

Order: Order list and order detail screens.

Dependency Injection

Uses Hilt for managing dependencies across the app.

Utils

Contains extension functions and helper utilities.


### Clean Architecture Layers
| Layer | Responsibility |
|---|---|
| **Domain** | Pure business rules. No Android dependencies. |
| **Data** | API + Room implementations. Maps DTOs/entities → domain models. |
| **Presentation** | ViewModels + Compose UI. Observes StateFlow, dispatches actions. |


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

