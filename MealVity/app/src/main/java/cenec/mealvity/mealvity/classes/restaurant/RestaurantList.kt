package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class RestaurantList(
    @SerializedName("businesses")
    var listRestaurant: ArrayList<Restaurant>
) {

    fun filterList(): ArrayList<Restaurant> {
        val auxList = arrayListOf<Restaurant>()

        for (listing in listRestaurant) {
            if (listing.isInfoComplete()) {
                auxList.add(listing)
                if (listing.price.isNullOrEmpty()) {
                    listing.price = "€€ - €€€"
                }
            }
        }
        listRestaurant.clear()
        listRestaurant.addAll(auxList)

        return listRestaurant
    }

    fun filterListByBestMatch(): ArrayList<Restaurant> {
        return listRestaurant
    }

    fun filterListByRatings(): ArrayList<Restaurant> {
        val sortListRatings = arrayListOf<Restaurant>()
        sortListRatings.addAll(listRestaurant)
        Collections.sort(sortListRatings, SortRestaurantListByRating())
        return sortListRatings
    }

    fun filterListByDistance(): ArrayList<Restaurant> {
        val sortListDistance = arrayListOf<Restaurant>()
        sortListDistance.addAll(listRestaurant)
        Collections.sort(sortListDistance, SortRestaurantListByDistance())
        return sortListDistance
    }

    fun filterListByEconomicPrice(): ArrayList<Restaurant> {
        val sortListEconomicPrice = arrayListOf<Restaurant>()
        sortListEconomicPrice.addAll(listRestaurant)
        Collections.sort(sortListEconomicPrice, SortRestaurantListByEconomicPrice())
        return sortListEconomicPrice
    }

    fun filterListByLuxuriousPrice(): ArrayList<Restaurant> {
        val sortListLuxuriousPrice = arrayListOf<Restaurant>()
        sortListLuxuriousPrice.addAll(listRestaurant)
        Collections.sort(sortListLuxuriousPrice, SortRestaurantListByLuxuriousPrice())
        return sortListLuxuriousPrice
    }

}