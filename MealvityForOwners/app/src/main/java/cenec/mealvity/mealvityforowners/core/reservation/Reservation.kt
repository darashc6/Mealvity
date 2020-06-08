package cenec.mealvity.mealvityforowners.core.reservation


data class Reservation(
    var user: UserReservationDetails?,
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