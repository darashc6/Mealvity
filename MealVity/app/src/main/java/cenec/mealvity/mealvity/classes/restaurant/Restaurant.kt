package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName

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

    fun isInfoComplete(): Boolean {
        if (phone.isNullOrEmpty() || location.address.isNullOrEmpty()) {
            return false
        }

        return true
    }

    fun displayCategories(): String {
        var stringCategories: String = ""

        for (category in categories) {
            stringCategories += category.title + ", "
        }

        return stringCategories.subSequence(0, stringCategories.length-2).toString()
    }

}