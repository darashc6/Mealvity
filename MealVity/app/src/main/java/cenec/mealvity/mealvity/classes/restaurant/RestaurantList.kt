package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName

data class RestaurantList(
    @SerializedName("businesses")
    var listRestaurant: ArrayList<Restaurant>
) {

    fun filterList(): ArrayList<Restaurant> {
        var filteredList = arrayListOf<Restaurant>()

        for (listing in listRestaurant) {
            if (listing.isInfoComplete()) {
                filteredList.add(listing)
            }
        }

        return filteredList
    }

}