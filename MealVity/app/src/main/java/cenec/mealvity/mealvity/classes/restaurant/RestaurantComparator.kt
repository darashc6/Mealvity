package cenec.mealvity.mealvity.classes.restaurant

class SortRestaurantListByRating: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return when {
            res1!!.rating == res2!!.rating -> 0
            res1.rating > res2.rating -> -1
            else -> 1
        }
    }
}

class SortRestaurantListByDistance: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return when {
            res1!!.distance == res2!!.distance -> 0
            res1.distance > res2.distance -> 1
            else -> -1
        }
    }
}

class SortRestaurantListByEconomicPrice: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return res1!!.price.compareTo(res2!!.price)
    }
}

class SortRestaurantListByLuxuriousPrice: Comparator<Restaurant> {
    override fun compare(res1: Restaurant?, res2: Restaurant?): Int {
        return -(res1!!.price.compareTo(res2!!.price))
    }
}