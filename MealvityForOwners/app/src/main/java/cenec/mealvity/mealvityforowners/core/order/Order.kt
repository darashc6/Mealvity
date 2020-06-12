package cenec.mealvity.mealvityforowners.core.order

data class Order (
    var orderCart: OrderCart?,
    var paymentMethod: PaymentMethod?,
    var user: UserDetails?,
    var restaurantName: String,
    var addressSelected: String,
    var expectedDeliveryTime: String,
    var deliveryMode: DeliveryMode? = null,
    var referenceNumber: String = "",
    var orderStatus: OrderStatus = OrderStatus.PENDING,
    var rejectionReason: String = ""
) {
    constructor():this(null, null , null, "", "", "")

    enum class PaymentMethod {
        CASH, CARD, PAYPAL
    }

    enum class OrderStatus {
        PENDING, ACCEPTED, REJECTED
    }

    enum class DeliveryMode {
        PICKUP, DELIVERY
    }
}