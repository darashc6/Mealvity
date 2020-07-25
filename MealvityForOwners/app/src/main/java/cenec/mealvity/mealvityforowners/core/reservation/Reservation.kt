package cenec.mealvity.mealvityforowners.core.reservation

import cenec.mealvity.mealvityforowners.core.order.UserDetails

/**
 * Class acting as a table reservation
 * @param user Specific user details
 * @param restaurantName Name of the restaurant where the table is being reserved
 * @param date Date of reservation
 * @param time Time of reservation
 * @param nGuests Nº of guests
 * @param referenceNumber Reference number, to identify the reservation
 * @param reservationStatus Status of the reservation (PENDING, ACCEPTED, REJECTED)
 * @param rejectionReason In case of rejection, reason
 */
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