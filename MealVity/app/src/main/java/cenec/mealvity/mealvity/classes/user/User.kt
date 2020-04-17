package cenec.mealvity.mealvity.classes.user

import java.io.Serializable

data class User(var fullName: String?,
                var phoneNumber: String?,
                var email: String?,
                var addresses: ArrayList<Address> = arrayListOf(),
                var orders: ArrayList<Orders> = arrayListOf()): Serializable {

    constructor(): this(null, null, null)

}