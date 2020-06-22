package cenec.mealvity.mealvity.classes.singleton

import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo

class RestaurantMoreInfoSingleton {
    companion object {
        private var restaurantMoreInfoSingleton: RestaurantMoreInfoSingleton? = null
        private var restaurantMoreInfo: RestaurantMoreInfo? = null

        fun getInstance(): RestaurantMoreInfoSingleton {
            if (restaurantMoreInfoSingleton == null) {
                restaurantMoreInfoSingleton = RestaurantMoreInfoSingleton()
            }

            return restaurantMoreInfoSingleton!!
        }
    }

    fun setRestaurantMoreInfo(newRestaurantMoreInfo: RestaurantMoreInfo) {
        restaurantMoreInfo = newRestaurantMoreInfo
    }

    fun getRestaurantMoreInfo(): RestaurantMoreInfo? {
        return restaurantMoreInfo
    }
}