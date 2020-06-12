package cenec.mealvity.mealvityforowners.core

import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.reservation.Reservation

data class RestaurantDatabase(
    var name: String,
    var orders: ArrayList<Order> = arrayListOf(),
    var reservations: ArrayList<Reservation> = arrayListOf()
) {
    constructor(): this("", arrayListOf(), arrayListOf())

    fun showPendingReservations(): ArrayList<Reservation> {
        val pendingReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.PENDING) {
                pendingReservationList.add(reservation)
            }
        }

        return pendingReservationList
    }

    fun showAcceptedReservations(): ArrayList<Reservation> {
        val acceptedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.ACCEPTED) {
                acceptedReservationList.add(reservation)
            }
        }

        return acceptedReservationList
    }

    fun showRejectedReservations(): ArrayList<Reservation> {
        val rejectedReservationList = arrayListOf<Reservation>()

        for (reservation in reservations) {
            if (reservation.reservationStatus == Reservation.ReservationStatus.REJECTED) {
                rejectedReservationList.add(reservation)
            }
        }

        return rejectedReservationList
    }

    fun showPendingOrders(): ArrayList<Order> {
        val pendingOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.PENDING) {
                pendingOrderList.add(order)
            }
        }

        return pendingOrderList
    }

    fun showAcceptedOrders(): ArrayList<Order> {
        val acceptedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.ACCEPTED) {
                acceptedOrderList.add(order)
            }
        }

        return acceptedOrderList
    }

    fun showRejectedOrders(): ArrayList<Order> {
        val rejectedOrderList = arrayListOf<Order>()

        for (order in orders) {
            if (order.orderStatus == Order.OrderStatus.REJECTED) {
                rejectedOrderList.add(order)
            }
        }

        return rejectedOrderList
    }
}