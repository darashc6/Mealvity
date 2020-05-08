package cenec.mealvity.mealvity.classes.autocompleteaddress

import com.google.gson.annotations.SerializedName

class StreetList (
    @SerializedName("items")
    var streetList: ArrayList<StreetName>
)

data class StreetName (
    @SerializedName("title")
    var streetName: String
)