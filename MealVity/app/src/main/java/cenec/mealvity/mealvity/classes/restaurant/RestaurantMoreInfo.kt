package cenec.mealvity.mealvity.classes.restaurant

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class RestaurantMoreInfo(
    var name: String,
    @SerializedName("url")
    var yelp_url: String,
    var display_phone: String,
    var categories: List<Categories>,
    var location: DisplayAddress,
    var coordinates: Coordinates,
    var photos: List<String>,
    var hours: List<Hour>
){

    fun displayCategories(): String {
        var stringCategories = ""

        for (category in categories) {
            stringCategories += category.title + ", "
        }

        return stringCategories.subSequence(0, stringCategories.length-2).toString()
    }

    fun displayFullAddress(): String {
        var stringAddress = ""

        for (sAddress in location.display_address) {
            stringAddress += "$sAddress, "
        }

        return stringAddress.substring(0, stringAddress.length-2)
    }
}

data class Coordinates(
    var latitude: String,
    var longitude: String
)

data class DisplayAddress(
    var display_address: List<String>
)

data class Hour(
    var open: List<Open>
)

data class Open(
    var day: Int,
    var start: String,
    var end: String
) {

    fun formatDay(): String {
        var formattedDay = ""

        when (day) {
            0 -> formattedDay = "Monday"
            1 -> formattedDay = "Tuesday"
            2 -> formattedDay = "Wednesday"
            3 -> formattedDay = "Thursday"
            4 -> formattedDay = "Friday"
            5 -> formattedDay = "Saturday"
            6 -> formattedDay = "Sunday"
        }

        return formattedDay
    }

    fun formatHours(): String {
        var formattedStart = ""
        var formattedEnd = ""

        val listStart = start.toMutableList()
        val listEnd = end.toMutableList()

        listStart.add(2, ':')
        listEnd.add(2, ':')

        for (char in listStart) {
            formattedStart += char
        }

        for (char in listEnd) {
            formattedEnd += char
        }

        return "$formattedStart - $formattedEnd"
    }

}