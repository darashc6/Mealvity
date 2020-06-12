package cenec.mealvity.mealvityforowners

import cenec.mealvity.mealvityforowners.core.RestaurantDatabase

class RestaurantDatabaseSingleton {
    companion object {
        private var rdSingleton: RestaurantDatabaseSingleton? = null
        private var restaurantDatabase: RestaurantDatabase? = null
        private var rdListener: RestaurantDatabaseSingletonListener? = null

        fun getInstance(): RestaurantDatabaseSingleton {
            if (rdSingleton == null) {
                rdSingleton = RestaurantDatabaseSingleton()
            }

            return rdSingleton!!
        }
    }

    fun setRestaurantDatabase(newRestaurantDatabase: RestaurantDatabase) {
        restaurantDatabase = newRestaurantDatabase
        rdListener?.onRestaurantDatabaseUpdated(newRestaurantDatabase)
    }

    fun getRestaurantDatabase(): RestaurantDatabase {
        return restaurantDatabase!!
    }

    fun setRestaurantDatabaseSingletonListener(newListener: RestaurantDatabaseSingletonListener) {
        rdListener = newListener
    }

    interface RestaurantDatabaseSingletonListener {
        fun onRestaurantDatabaseUpdated(updatedRestaurantDatabase: RestaurantDatabase)
    }
}