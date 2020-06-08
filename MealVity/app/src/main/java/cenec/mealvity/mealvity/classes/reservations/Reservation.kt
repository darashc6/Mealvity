package cenec.mealvity.mealvity.classes.reservations


import java.util.*

data class Reservation(
    var user: UserReservationDetails?,
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

    fun addGuest() {
        nGuests++
    }

    fun removeGuest() {
        if (nGuests > 1) {
            nGuests--
        }
    }

    enum class ReservationStatus {
        PENDING, ACCEPTED, REJECTED
    }
}