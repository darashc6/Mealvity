package cenec.mealvity.mealvity.classes.user

import java.io.Serializable

data class User(var fullName: String?,
                var phoneNumber: String?,
                var email: String?,
                var password: String?,
                var addresses: ArrayList<Class<Address>>? = null,
                var orders: String? = null): Serializable {

    constructor(): this(null, null, null, null)

}