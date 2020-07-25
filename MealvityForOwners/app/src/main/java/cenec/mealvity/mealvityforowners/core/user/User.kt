package cenec.mealvity.mealvityforowners.core.user

import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.reservation.Reservation
import java.io.Serializable

/**
 * Class acting as an user of the application
 * @param userId User's uid
 * @param fullName User's full name
 * @param phoneNumber User's phone number
 * @param email User's email
 * @param addresses List of addresses
 * @param orders List of orders
 * @param reservations List of reservations
 */
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