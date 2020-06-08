package cenec.mealvity.mealvityforowners

import cenec.mealvity.mealvityforowners.core.RestaurantDatabase

class RestaurantDatabaseSingleton {
    companion object {
        private var rdSingleton: RestaurantDatabaseSingleton? = null
        private var restaurantDatabase: RestaurantDatabase? = null

        fun getInstance(): RestaurantDatabaseSingleton {
            if (rdSingleton == null) {
                rdSingleton = RestaurantDatabaseSingleton()
            }

            return rdSingleton!!
        }
    }

    fun setRestaurantDatabase(newRestaurantDatabase: RestaurantDatabase) {
        restaurantDatabase = newRestaurantDatabase
    }

    fun getRestaurantDatabase(): RestaurantDatabase {
        return restaurantDatabase!!
    }
}