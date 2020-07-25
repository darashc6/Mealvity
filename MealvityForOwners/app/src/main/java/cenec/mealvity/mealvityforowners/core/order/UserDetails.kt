package cenec.mealvity.mealvityforowners.core.order

/**
 * Class acting as a User with specific details
 * @param userId User's uid
 * @param fullName Full name
 * @param phoneNumber Phone number
 * @param email Email
 */
data class UserDetails(
    var userId: String?,
    var fullName: String?,
    var phoneNumber: String?,
    var email: String?
) {
    constructor(): this(null, null, null, null)
}