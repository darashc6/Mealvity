package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class containing a list of Restaurant data
 * @param results List of Restaurant
 */
data class RestaurantList(
    @SerializedName("businesses")
    var results: ArrayList<Restaurant>
) {

    /**
     * Returns a RestaurantList with the Restaurant' full data
     * @return RestaurantList with Restaurant's full info
     */
    fun getListWithFullInfo(): RestaurantList {
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

    /**
     * Returns the original RestaurantList when first searched
     * @return Original RestaurantList
     */
    fun filterListByBestMatch(): RestaurantList {
        return this
    }

    /**
     * Returns a RestaurantList sorted by best Ratings
     * @return RestaurantList sorted by ratings
     */
    fun filterListByRatings(): RestaurantList {
        val sortListRatings = arrayListOf<Restaurant>()
        sortListRatings.addAll(results)
        Collections.sort(sortListRatings, SortRestaurantListByRating())
        return RestaurantList(sortListRatings)
    }

    /**
     * Returns a RestaurantList sorted by nearest distance
     * @return RestaurantList sorted by distance
     */
    fun filterListByDistance(): RestaurantList {
        val sortListDistance = arrayListOf<Restaurant>()
        sortListDistance.addAll(results)
        Collections.sort(sortListDistance, SortRestaurantListByDistance())
        return RestaurantList(sortListDistance)
    }

    /**
     * Returns a RestaurantList sorted by cheapest price
     * @return RestaurantList sorted by cheapest price
     */
    fun filterListByEconomicPrice(): RestaurantList {
        val sortListEconomicPrice = arrayListOf<Restaurant>()
        sortListEconomicPrice.addAll(results)
        Collections.sort(sortListEconomicPrice, SortRestaurantListByEconomicPrice())
        return RestaurantList(sortListEconomicPrice)
    }

    /**
     * Returns a RestaurantList sorted by most expensive price
     * @return RestaurantList sorted by most expensive price
     */
    fun filterListByLuxuriousPrice(): RestaurantList {
        val sortListLuxuriousPrice = arrayListOf<Restaurant>()
        sortListLuxuriousPrice.addAll(results)
        Collections.sort(sortListLuxuriousPrice, SortRestaurantListByLuxuriousPrice())
        return RestaurantList(sortListLuxuriousPrice)
    }

}