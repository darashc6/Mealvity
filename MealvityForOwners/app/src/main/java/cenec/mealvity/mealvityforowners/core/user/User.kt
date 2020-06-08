package cenec.mealvity.mealvityforowners.core.user

import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.reservation.Reservation
import java.io.Serializable

data class User(
    var userId: String?,
    var fullName: String?,
    var phoneNumber: String?,
    var email: String?,
    var addresses: ArrayList<Address> = arrayListOf(),
    var orders: ArrayList<Order> = arrayListOf(),
    var reservations: ArrayList<Reservation> = arrayListOf()
) : Serializable {
    constructor(): this(null, null, null, null)
}