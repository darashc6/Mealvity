package cenec.mealvity.mealvity.classes.autocompleteaddress

import com.google.gson.annotations.SerializedName

/**
 * Class containting a list of StreetName
 * @param results List of StreetName
 */
class StreetList (
    @SerializedName("items")
    var results: ArrayList<StreetName>
)

/**
 * Class containing a street name
 * @param streetName String with the street name
 */
data class StreetName (
    @SerializedName("title")
    var streetName: String
)