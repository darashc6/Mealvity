package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName

/**
 * Class containing all the Restaurant info
 * @param id Restaurant id
 * @param name Restaurant name
 * @param image_url Restaurant Image
 * @param categories List of categories
 * @param rating Restaurant rating
 * @param price Restaurant price range
 * @param location Restaurant address
 * @param phone Restaurant number
 * @param distance Restaurant distance from the search address
 */
data class Restaurant(
    var id: String,
    var name: String,
    var image_url: String,
    var categories: List<Categories>,
    var rating: Float,
    var price: String,
    var location: Location,
    @SerializedName("display_phone")
    var phone: String,
    var distance: Double
) {

    /**
     * Checks if the restaurant info is complete
     * @return true if it has all the info, false if otherwise
     */
    fun isInfoComplete(): Boolean {
        if (phone.isNullOrEmpty() || location.address.isNullOrEmpty()) {
            return false
        }

        return true
    }

    /**
     * Displays a list of categories in a String text
     * @return String with all the categories the restaurant has to offer
     */
    fun displayCategories(): String {
        var stringCategories = ""

        for (category in categories) {
            stringCategories += category.title + ", "
        }

        return stringCategories.subSequence(0, stringCategories.length-2).toString()
    }

}

/**
 * Class containing the category title
 * @param title Category title
 */
data class Categories(
    var title: String
)

/**
 * Class containing the restaurant address
 * @param address Restaurant address
 */
data class Location(
    @SerializedName("address1")
    var address: String
)