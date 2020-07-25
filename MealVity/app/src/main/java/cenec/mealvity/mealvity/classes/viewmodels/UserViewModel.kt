package cenec.mealvity.mealvity.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cenec.mealvity.mealvity.classes.user.User

/**
 * ViewModel of the User class
 */
class UserViewModel: ViewModel() {
    private var userLiveData: MutableLiveData<User> = MutableLiveData()

    /**
     * Sets the User LiveData value
     * @param updatedUser New LiveData value
     */
    fun setUserLiveData(updatedUser: User) {
        userLiveData.value = updatedUser
    }

    /**
     * Returns the LiveData value of the User
     * @return LiveData value
     */
    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }
}