package cenec.mealvity.mealvity.classes.user

import cenec.mealvity.mealvity.classes.reservations.Reservation
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

    constructor() : this(null, null, null, null)

    /**
     * Adds an order to the user's list
     * @param newOrder New order to add
     */
    fun addOrder(newOrder: Order) {
        orders.add(newOrder)
    }

    /**
     * Adds a reservations to the user's list
     * @param newReservation New reservation to add
     */
    fun addReservation(newReservation: Reservation) {
        reservations.add(newReservation)
    }

    /**
     * Shows all types of reservations (PENDING, ACCEPTED, REJECTED)
     * @return List of all reservations
     */
    fun showAllReservations(): ArrayList<Reservation> {
        return reverseReservationList(reservations)
    }

    /**
     * Shows reservations that are PENDING
     * @return List of PENDING reservations
     */
    fun showPendingReservations(): ArrayList<Reservation> {
        val pendingReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.PENDING) {
                pendingReservationList.add(reservation)
            }
        }

        return reverseReservationList(pendingReservationList)
    }

    /**
     * Shows reservations that are ACCEPTED
     * @return List of ACCEPTED reservations
     */
    fun showAcceptedReservations(): ArrayList<Reservation> {
        val acceptedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.ACCEPTED) {
                acceptedReservationList.add(reservation)
            }
        }

        return reverseReservationList(acceptedReservationList)
    }

    /**
     * Shows reservations that are REJECTED
     * @return List of REJECTED reservations
     */
    fun showRejectedReservations(): ArrayList<Reservation> {
        val rejectedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.REJECTED) {
                rejectedReservationList.add(reservation)
            }
        }

        return reverseReservationList(rejectedReservationList)
    }

    /**
     * Shows all types of orders (ACCEPTED, REJECTED, PENDING)
     * @return List of all orders
     */
    fun showAllOrders(): ArrayList<Order> {
        return reverseOrderList(orders)
    }

    /**
     * Reverses a given reservation list
     * @param listToReverse Reservation list to reverse
     */
    private fun reverseReservationList(listToReverse: ArrayList<Reservation>): ArrayList<Reservation> {
        val reversedReservationList = arrayListOf<Reservation>()

        if (listToReverse.isNotEmpty()) {
            for (i in listToReverse.size-1 downTo 0) {
                reversedReservationList.add(listToReverse[i])
            }
        }

        return reversedReservationList
    }

    /**
     * Reverses a given order list
     * @param listToReverse Order list to reverse
     */
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