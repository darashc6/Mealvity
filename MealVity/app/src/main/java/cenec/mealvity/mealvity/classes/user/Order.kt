package cenec.mealvity.mealvity.classes.user

import cenec.mealvity.mealvity.classes.orders.OrderCart
import java.util.*

/**
 * TODO
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
