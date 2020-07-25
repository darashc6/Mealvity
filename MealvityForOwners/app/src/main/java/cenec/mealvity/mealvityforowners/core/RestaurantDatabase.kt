package cenec.mealvity.mealvityforowners.core


import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.reservation.Reservation

/**
 * Class acting as a database for the restaurant
 * @param name Name of the restaurant
 * @param orders List of orders the restaurant has received
 * @param reservations List of reservations the restaurant has received
 */
data class RestaurantDatabase(
    var name: String,
    var orders: ArrayList<Order> = arrayListOf(),
    var reservations: ArrayList<Reservation> = arrayListOf()
) {
    constructor(): this("", arrayListOf(), arrayListOf())

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
     * Shows orders that are PENDING
     * @return List of PENDING orders
     */
    fun showPendingOrders(): ArrayList<Order> {
        val pendingOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.PENDING) {
                pendingOrderList.add(order)
            }
        }

        return reverseOrderList(pendingOrderList)
    }

    /**
     * Shows orders that are ACCEPTED
     * @return List of ACCEPTED orders
     */
    fun showAcceptedOrders(): ArrayList<Order> {
        val acceptedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.ACCEPTED) {
                acceptedOrderList.add(order)
            }
        }

        return reverseOrderList(acceptedOrderList)
    }

    /**
     * Shows reservations that are REJECTED
     * @return List of REJECTED orders
     */
    fun showRejectedOrders(): ArrayList<Order> {
        val rejectedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.REJECTED) {
                rejectedOrderList.add(order)
            }
        }

        return reverseOrderList(rejectedOrderList)
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
     * Reverses a given orders list
     * @param listToReverse order list to reverse
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