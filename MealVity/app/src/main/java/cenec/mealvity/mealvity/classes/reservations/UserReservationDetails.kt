package cenec.mealvity.mealvity.classes.reservations

data class UserReservationDetails(
    var userId: String?,
    var fullName: String?,
    var phoneNumber: String?,
    var email: String?
) {
    constructor(): this(null, null, null, null)
}