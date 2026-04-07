# OurMall — System Design

## 1. Multi-Vendor Order Structure

A single customer `Order` acts as the **parent record** that groups everything:

```
Order
├── id                        (e.g. ORD-A3F9C1B2)
├── createdAt
├── status                    (PENDING | CONFIRMED | PARTIALLY_CANCELLED | CANCELLED | COMPLETED)
├── cartLevelDiscountAmount   (from promo code)
├── promoCode
└── vendorOrders: List<VendorOrder>
    ├── VendorOrder (Vendor A)
    │   ├── vendorId / vendorName
    │   ├── subtotal
    │   └── items: List<OrderItem>
    │       ├── id, productId, productName, imageUrl
    │       ├── quantity, unitPrice, discountAmount
    │       ├── status  (PENDING → CONFIRMED → SHIPPED → DELIVERED / CANCELLED)
    │       └── refundAmount
    └── VendorOrder (Vendor B)
        └── items: ...
```

**Key design decisions:**
- The `Order` owns the `cartLevelDiscount` (applied once at the top).  
- Each `VendorOrder` owns its items and its `subtotal` (sum of active item line totals).  
- `grandTotal` on the `Order` is computed: `sum(vendorOrder.activeSubtotal) − cartLevelDiscountAmount`.  
- Totals are **never stored** — they are computed from live item data, so cancellations are automatically reflected.

---

## 2. How an Order Splits Per Vendor

When a customer places an order the flow is:

```
Cart  ──►  Pre-Checkout Validation  ──►  createOrder()
                                              │
                           cart.vendorCarts.forEach { vendorCart →
                               VendorOrder(
                                   vendorId   = vendorCart.vendorId,
                                   vendorName = vendorCart.vendorName,
                                   items      = vendorCart.items.map { cartItem →
                                       OrderItem(
                                           id            = "${orderId}-${vendorId}-${index}",
                                           unitPrice     = cartItem.snapshotPrice,  // locked at checkout
                                           discountAmount = cartItem.appliedProductDiscount,
                                           status        = PENDING,
                                       )
                                   },
                                   subtotal = vendorCart.subtotal,
                               )
                           }
```

Each `VendorOrder` is a logical sub-order under the parent `Order`.  
In a production system each `VendorOrder` would be sent as a separate fulfilment request to the respective vendor's fulfilment API, and its `items` track status independently.

---

## 3. Item-Level Cancellation & Refund

```
cancelOrderItem(orderId, itemId)
    │
    ├─ Validate: item.canBeCancelled
    │       (only PENDING or CONFIRMED items can be cancelled)
    │
    ├─ Mark item.status = CANCELLED
    │
    ├─ Set item.refundAmount = item.lineTotal
    │       lineTotal = (unitPrice − discountAmount) × quantity
    │
    ├─ Recalculate order totals (computed from live data — no update needed)
    │
    └─ Determine new order-level status:
           ALL items CANCELLED           → OrderStatus.CANCELLED
           SOME items CANCELLED          → OrderStatus.PARTIALLY_CANCELLED
           No cancellations              → unchanged

cancelOrder(orderId)
    └─ Calls the above logic for every item where canBeCancelled == true
       Items already SHIPPED or DELIVERED are left unchanged
```

**Refund scope rule:** Only `item.lineTotal` is refunded per cancelled item.  
The `cartLevelDiscountAmount` is **not** re-distributed — this keeps refund logic simple and deterministic.  
In production, partial promo refund rules would be defined per business policy.

---

## 4. Basic API Structure

### Add to Cart
```
POST /cart/items
Body:  { productId, quantity }
Logic: • Fetch fresh product price from products service
       • Validate stock ≥ quantity
       • Merge if productId already exists (increment quantity)
       • Store snapshot price (locked at time of add)
Response: { cartItem }

PATCH /cart/items/{productId}
Body:  { quantity }   // quantity = 0 removes the item

DELETE /cart/items/{productId}

POST /cart/promo
Body:  { code }
Response: { discountPercent, description }
```

### Place Order
```
POST /orders
Body:  { cartId }
Server logic:
  1. Re-validate all cart items (stock, price freshness, offer expiry)
  2. Split cart into VendorOrders (group by vendorId)
  3. Lock prices at current snapshot
  4. Create parent Order + child VendorOrders
  5. Deduct stock per item
  6. Notify each vendor's fulfilment service
  7. Clear customer cart
Response: { orderId, vendorOrders[], grandTotal }
```

### Cancel Order / Item
```
DELETE /orders/{orderId}
Logic: Cancel all PENDING/CONFIRMED items → compute refunds → update status

DELETE /orders/{orderId}/items/{itemId}
Logic: • Check item.status in [PENDING, CONFIRMED]
       • Set status = CANCELLED
       • Set refundAmount = lineTotal
       • Recalculate order totals
       • If all items cancelled → order status = CANCELLED
       • Else → order status = PARTIALLY_CANCELLED
       • Trigger refund to payment provider
Response: { updatedOrder }
```

### Other Key Endpoints
```
GET  /products?query&category&minPrice&maxPrice&stock&page&pageSize
GET  /products/{id}
GET  /orders                    // paginated order history
GET  /orders/{id}
POST /cart/validate             // pre-checkout validation pass
```
