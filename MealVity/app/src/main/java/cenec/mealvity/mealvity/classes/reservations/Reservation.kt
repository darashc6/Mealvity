package cenec.mealvity.mealvity.classes.reservations


import cenec.mealvity.mealvity.classes.user.UserDetails
import java.util.*

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
    var nGuests: Int = 1,
    var referenceNumber: String = "",
    var reservationStatus: ReservationStatus = ReservationStatus.PENDING,
    var rejectionReason: String = ""
) {

    constructor(): this(null)

    init {
        referenceNumber = generateReferenceNumber()
    }

    /**
     * Returns a custom generated reference number
     * @return String with generated reference number
     */
    private fun generateReferenceNumber(): String {
        val charList = charArrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9')
        val random = Random()
        var ref = "MVTR"

        for (i in 0..5) {
            val num = random.nextInt(charList.size)
            ref += charList[num]
        }

        return ref
    }

    /**
     * Adds a guest
     */
    fun addGuest() {
        nGuests++
    }

    /**
     * Removes a guest
     */
    fun removeGuest() {
        if (nGuests > 1) {
            nGuests--
        }
    }

    enum class ReservationStatus {
        PENDING, ACCEPTED, REJECTED
    }
}