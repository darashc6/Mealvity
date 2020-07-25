package cenec.mealvity.mealvity.classes.restaurant


import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Class acting as extra info of the restaurant
 * @param name Name of the restaurant
 * @param yelp_url URL of the restaurant0s yelp website
 * @param display_phone Phone number
 * @param categories Categories/Cuisines
 * @param location Address of the restaurant
 * @param coordinates Coordinates of the restaurant
 * @param photos List of urls containing the restaurant's photos
 * @param hours List of the restaurant's opening hours
 */
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
): Serializable{

    /**
     * Returns a string with all the categories/cuisines the restaurant has to offer
     * @return String with all the categories/cuisines
     */
    fun displayCategories(): String {
        var stringCategories = ""

        for (category in categories) {
            stringCategories += category.title + ", "
        }

        return stringCategories.subSequence(0, stringCategories.length-2).toString()
    }

    /**
     * Returns a string displaying the restaurant's full address
     * @return String with full address
     */
    fun displayFullAddress(): String {
        var stringAddress = ""

        for (sAddress in location.display_address) {
            stringAddress += "$sAddress, "
        }

        return stringAddress.substring(0, stringAddress.length-2)
    }
}

/**
 * Class acting as coordinates
 * @param latitude Latitude value of coordinates
 * @param longitude Longitude value of coordinates
 */
data class Coordinates(
    var latitude: String,
    var longitude: String
)

/**
 * Class acting as a restaurant address
 * @param display_address List of different string components of an address
 */
data class DisplayAddress(
    var display_address: List<String>
)

/**
 * Class acting as opening hours
 * @param open List of opening hours
 */
data class Hour(
    var open: List<Open>
)

/**
 * Class acting as a timing
 * @param day Day
 * @param start Starting hour
 * @param end Closing hour
 */
data class Open(
    var day: Int,
    var start: String,
    var end: String
) {

    /**
     * Returns a string depending on the integer value of day
     * @return String value of day
     */
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

    /**
     * Returns a formatted value of start and end
     * @return String with formatted value of start and end
     */
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