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

    fun showAllReservations(): ArrayList<Reservation> {
        return reverseReservationList(reservations)
    }

    fun showPendingReservations(): ArrayList<Reservation> {
        val pendingReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.PENDING) {
                pendingReservationList.add(reservation)
            }
        }

        return reverseReservationList(pendingReservationList)
    }

    fun showAcceptedReservations(): ArrayList<Reservation> {
        val acceptedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.ACCEPTED) {
                acceptedReservationList.add(reservation)
            }
        }

        return reverseReservationList(acceptedReservationList)
    }

    fun showRejectedReservations(): ArrayList<Reservation> {
        val rejectedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.REJECTED) {
                rejectedReservationList.add(reservation)
            }
        }

        return reverseReservationList(rejectedReservationList)
    }

    fun showAllOrders(): ArrayList<Order> {
        return reverseOrderList(orders)
    }

    private fun reverseReservationList(listToReverse: ArrayList<Reservation>): ArrayList<Reservation> {
        val reversedReservationList = arrayListOf<Reservation>()

        if (listToReverse.isNotEmpty()) {
            for (i in listToReverse.size-1 downTo 0) {
                reversedReservationList.add(listToReverse[i])
            }
        }

        return reversedReservationList
    }

    private fun reverseOrderList(listToReverse: ArrayList<Order>): ArrayList<Order> {
        val reversedOrderList = arrayListOf<Order>()

        if (listToReverse.isNotEmpty()) {
            for (i in listToReverse.size-1 downTo 0) {
                reversedOrderList.add(listToReverse[i])
            }
        }

        return reversedOrderList
    }

}