package cenec.mealvity.mealvityforowners.core.order

data class Order (
    var orderCart: OrderCart?,
    var paymentMethod: PaymentMethod?,
    var referenceNumber: String
) {
    constructor(): this(null, null, "")

    enum class PaymentMethod {
        CASH, CARD, PAYPAL
    }
}