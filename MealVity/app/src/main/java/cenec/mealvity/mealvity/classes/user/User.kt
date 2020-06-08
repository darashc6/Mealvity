package cenec.mealvity.mealvity.classes.user

import cenec.mealvity.mealvity.classes.reservations.Reservation
import java.io.Serializable

/**
 * Class acting as an user of the application
 * @param fullName User's full name
 * @param phoneNumber User's phone number
 * @param email User's email
 * @param addresses User's list of addresses
 * @param orders User's list of orders
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

    constructor() : this(null, null, null, null)

    fun addOrder(newOrder: Order) {
        orders.add(newOrder)
    }

    fun addReservation(newReservation: Reservation) {
        reservations.add(newReservation)
    }

}