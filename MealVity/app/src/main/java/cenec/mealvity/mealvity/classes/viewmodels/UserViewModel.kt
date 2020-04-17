package cenec.mealvity.mealvity.classes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cenec.mealvity.mealvity.classes.user.User

class UserViewModel: ViewModel() {
    private var userLiveData: MutableLiveData<User> = MutableLiveData()

    fun setUserLiveData(updatedUser: User) {
        userLiveData.value = updatedUser
    }

    fun getUserLiveData(): LiveData<User> {
        return userLiveData
    }
}