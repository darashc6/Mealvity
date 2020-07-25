package cenec.mealvity.mealvityforowners.core.order

/**
 * Class acting as an order
 * @param orderCart Cart of the order
 * @param paymentMethod Payment method of the order (CASH, CARD, PAYPAL)
 * @param user Specific details of the user
 * @param restaurantName Name of the restaurant where the order is made
 * @param addressSelected Address selected for delivery
 * @param expectedDeliveryTime Expected delivery time
 * @param deliveryMode Mode of delivery (PICK-UP, DELIVERY)
 * @param referenceNumber Reference number for identifications
 * @param orderStatus Status of the order (PENDING, ACCEPTED, REJECTED)
 * @param rejectionReason Reason in case order is REJECTED
 */
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