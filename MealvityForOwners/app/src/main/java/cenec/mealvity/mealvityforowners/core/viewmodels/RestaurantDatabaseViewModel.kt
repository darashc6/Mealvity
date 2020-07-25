package cenec.mealvity.mealvityforowners.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase

/**
 * ViewModel of the RestaurantDatabase class
 */
class RestaurantDatabaseViewModel: ViewModel() {
    private var restaurantDatabase = MutableLiveData<RestaurantDatabase>()

    /**
     * Sets the RestaurantDatabase LiveData value
     * @param newRestaurantDatabase New LiveData value
     */
    fun setRestaurantDatabase(newRestaurantDatabase: RestaurantDatabase) {
        restaurantDatabase.value = newRestaurantDatabase
    }

    /**
     * Returns the LiveData valuw of the user
     * @return LiveData value
     */
    fun getRestaurantDatabase(): LiveData<RestaurantDatabase> {
        return restaurantDatabase
    }
}