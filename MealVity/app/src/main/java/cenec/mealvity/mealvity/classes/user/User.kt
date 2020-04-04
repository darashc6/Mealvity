package cenec.mealvity.mealvity.classes.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.Serializable

data class User(var fullName: String?,
                var phoneNumber: String?,
                var email: String?,
                var password: String?,
                var addresses: List<Address>? = null,
                var orders: String? = null): Serializable {

    private var userLiveData: MutableLiveData<User> = MutableLiveData()

    constructor(): this(null, null, null, null)

    fun updateUserLiveData() {
        userLiveData.value=this
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }

}