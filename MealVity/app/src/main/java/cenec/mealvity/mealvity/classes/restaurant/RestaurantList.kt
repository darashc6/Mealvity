package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class RestaurantList(
    @SerializedName("businesses")
    var results: ArrayList<Restaurant>
) {

    fun filterList(): RestaurantList {
        val auxList = arrayListOf<Restaurant>()

        for (listing in results) {
            if (listing.isInfoComplete()) {
                auxList.add(listing)
                if (listing.price.isNullOrEmpty()) {
                    listing.price = "€€ - €€€"
                }
            }
        }
        results.clear()
        results.addAll(auxList)

        return RestaurantList(results)
    }

    fun filterListByBestMatch(): RestaurantList {
        return this
    }

    fun filterListByRatings(): RestaurantList {
        val sortListRatings = arrayListOf<Restaurant>()
        sortListRatings.addAll(results)
        Collections.sort(sortListRatings, SortRestaurantListByRating())
        return RestaurantList(sortListRatings)
    }

    fun filterListByDistance(): RestaurantList {
        val sortListDistance = arrayListOf<Restaurant>()
        sortListDistance.addAll(results)
        Collections.sort(sortListDistance, SortRestaurantListByDistance())
        return RestaurantList(sortListDistance)
    }

    fun filterListByEconomicPrice(): RestaurantList {
        val sortListEconomicPrice = arrayListOf<Restaurant>()
        sortListEconomicPrice.addAll(results)
        Collections.sort(sortListEconomicPrice, SortRestaurantListByEconomicPrice())
        return RestaurantList(sortListEconomicPrice)
    }

    fun filterListByLuxuriousPrice(): RestaurantList {
        val sortListLuxuriousPrice = arrayListOf<Restaurant>()
        sortListLuxuriousPrice.addAll(results)
        Collections.sort(sortListLuxuriousPrice, SortRestaurantListByLuxuriousPrice())
        return RestaurantList(sortListLuxuriousPrice)
    }

}