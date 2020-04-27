package cenec.mealvity.mealvity.classes.restaurant

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address1")
    var address: String
)