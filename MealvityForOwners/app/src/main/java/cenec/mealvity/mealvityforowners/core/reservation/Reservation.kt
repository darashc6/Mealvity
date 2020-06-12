package cenec.mealvity.mealvityforowners.core.reservation

import cenec.mealvity.mealvityforowners.core.order.UserDetails


data class Reservation(
    var user: UserDetails?,
    var restaurantName: String = "",
    var date: String = "",
    var time: String = "",
    var nGuests: Int = 0,
    var referenceNumber: String = "",
    var reservationStatus: ReservationStatus = ReservationStatus.PENDING,
    var rejectionReason: String = ""
) {

    constructor(): this(null)


    enum class ReservationStatus {
        PENDING, ACCEPTED, REJECTED
    }
}