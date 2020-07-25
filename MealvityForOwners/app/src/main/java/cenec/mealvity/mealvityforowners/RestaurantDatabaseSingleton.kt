package cenec.mealvity.mealvityforowners

import cenec.mealvity.mealvityforowners.core.RestaurantDatabase

/**
 * Static class used for saving a RestaurantDatabase object
 * Used for communcating between activities
 */
class RestaurantDatabaseSingleton {
    companion object {
        private var rdSingleton: RestaurantDatabaseSingleton? = null // Instance of the singleton
        private var restaurantDatabase: RestaurantDatabase? = null // Instance of the object

        /**
         * Returns an instance of the singleton
         * @return Instance of the singleton class
         */
        fun getInstance(): RestaurantDatabaseSingleton {
            if (rdSingleton == null) {
                rdSingleton = RestaurantDatabaseSingleton()
            }

            return rdSingleton!!
        }
    }

    /**
     * Sets a new RestaurantDatabase object in the singleton
     * @param newRestaurantDatabase New object stores in the singleton
     */
    fun setRestaurantDatabase(newRestaurantDatabase: RestaurantDatabase) {
        restaurantDatabase = newRestaurantDatabase
    }

    /**
     * Returns the RestaurantDatabase object saved in the singleton
     * @return Object saved in the singleton
     */
    fun getRestaurantDatabase(): RestaurantDatabase {
        return restaurantDatabase!!
    }
}