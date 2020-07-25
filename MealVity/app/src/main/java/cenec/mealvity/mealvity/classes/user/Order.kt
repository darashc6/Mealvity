package cenec.mealvity.mealvity.classes.user

import cenec.mealvity.mealvity.classes.orders.OrderCart
import java.util.*

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
data class Order(
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

    init {
        referenceNumber = generateReferenceNumber()
    }

    /**
     * Returns a custom generated reference number for identification
     * @return String with reference number
     */
    private fun generateReferenceNumber(): String {
        val charList = charArrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '1','2','3','4','5','6','7','8','9')
        val random = Random()
        var ref = "MVTD"

        for (i in 0..5) {
            val num = random.nextInt(charList.size)
            ref += charList[num]
        }

        return ref
    }

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
