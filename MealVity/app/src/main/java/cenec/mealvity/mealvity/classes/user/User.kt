package cenec.mealvity.mealvity.classes.user

data class User(var fullName: String?,
                var phoneNumber: String?,
                var email: String?,
                var password: String?,
                var addresses: ArrayList<Class<Address>>? = null,
                var orders: String? = null) {

    constructor(): this(null, null, null, null)

}