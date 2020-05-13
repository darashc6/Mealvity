package cenec.mealvity.mealvity.classes.restaurant

/**
 * Class used for sorting a list of restaurants by the best rating
 */
class SortRestaurantListByRating: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return when {
            res1!!.rating == res2!!.rating -> 0
            res1.rating > res2.rating -> -1
            else -> 1
        }
    }
}

/**
 * Class used for sorting a list of restaurants by the nearest distance
 */
class SortRestaurantListByDistance: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return when {
            res1!!.distance == res2!!.distance -> 0
            res1.distance > res2.distance -> 1
            else -> -1
        }
    }
}

/**
 * Class used for sorting a list of restaurants by the cheapest price
 */
class SortRestaurantListByEconomicPrice: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return res1!!.price.compareTo(res2!!.price)
    }
}

/**
 * Class used for sorting a list of restaurants by the most expensive prive
 */
class SortRestaurantListByLuxuriousPrice: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return -(res1!!.price.compareTo(res2!!.price))
    }
}