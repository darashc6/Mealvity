package cenec.mealvity.mealvityforowners.core.order



data class UserDetails(
    var userId: String?,
    var fullName: String?,
    var phoneNumber: String?,
    var email: String?
) {
    constructor(): this(null, null, null, null)
}