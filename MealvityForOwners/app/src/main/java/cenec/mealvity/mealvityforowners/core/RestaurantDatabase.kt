package cenec.mealvity.mealvityforowners.core

import androidx.core.content.ContextCompat
import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.reservation.Reservation


data class RestaurantDatabase(
    var name: String,
    var orders: ArrayList<Order> = arrayListOf(),
    var reservations: ArrayList<Reservation> = arrayListOf()
) {
    constructor(): this("", arrayListOf(), arrayListOf())

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

    fun showPendingOrders(): ArrayList<Order> {
        val pendingOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.PENDING) {
                pendingOrderList.add(order)
            }
        }

        return reverseOrderList(pendingOrderList)
    }

    fun showAcceptedOrders(): ArrayList<Order> {
        val acceptedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.ACCEPTED) {
                acceptedOrderList.add(order)
            }
        }

        return reverseOrderList(acceptedOrderList)
    }

    fun showRejectedOrders(): ArrayList<Order> {
        val rejectedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.REJECTED) {
                rejectedOrderList.add(order)
            }
        }

        return reverseOrderList(rejectedOrderList)
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