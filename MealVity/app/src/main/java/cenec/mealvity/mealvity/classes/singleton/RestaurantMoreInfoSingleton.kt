package cenec.mealvity.mealvity.classes.singleton

import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo

/**
 * Static class used for saving a RestaurantMoreInfo object
 * Used for communicating between activities
 */
class RestaurantMoreInfoSingleton {
    companion object {
        private var restaurantMoreInfoSingleton: RestaurantMoreInfoSingleton? = null // Instance of the singleton
        private var restaurantMoreInfo: RestaurantMoreInfo? = null // Instance of the RestaurantMoreInfo object

        /**
         * Returns an instance of the singleton
         * @return Instance of the singleton class
         */
        fun getInstance(): RestaurantMoreInfoSingleton {
            if (restaurantMoreInfoSingleton == null) {
                restaurantMoreInfoSingleton = RestaurantMoreInfoSingleton()
            }

            return restaurantMoreInfoSingleton!!
        }
    }

    /**
     * Sets a new RestaurantMoreInfo object in the singleton
     * @param newRestaurantMoreInfo New RestaurantMoreInfo object
     */
    fun setRestaurantMoreInfo(newRestaurantMoreInfo: RestaurantMoreInfo) {
        restaurantMoreInfo = newRestaurantMoreInfo
    }

    /**
     * Returns the object saved in the singleton
     * @return RestaurantMoreInfo object saved in the singleton
     */
    fun getRestaurantMoreInfo(): RestaurantMoreInfo? {
        return restaurantMoreInfo
    }
}