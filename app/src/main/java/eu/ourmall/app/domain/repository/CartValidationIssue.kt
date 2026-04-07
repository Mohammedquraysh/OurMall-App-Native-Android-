package eu.ourmall.app.domain.repository


data class CartValidationIssue(
    val productId: String,
    val productName: String,
    val issueType: CartIssueType,
    val detail: String,
)

enum class CartIssueType {
    PRICE_CHANGED,
    OUT_OF_STOCK,
    OFFER_EXPIRED,
    INSUFFICIENT_STOCK,
}