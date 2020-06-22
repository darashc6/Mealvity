package cenec.mealvity.mealvityforowners.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase

class RestaurantDatabaseViewModel: ViewModel() {
    private var restaurantDatabase = MutableLiveData<RestaurantDatabase>()

    fun setRestaurantDatabase(newRestaurantDatabase: RestaurantDatabase) {
        restaurantDatabase.value = newRestaurantDatabase
    }

    fun getRestaurantDatabase(): LiveData<RestaurantDatabase> {
        return restaurantDatabase
    }
}