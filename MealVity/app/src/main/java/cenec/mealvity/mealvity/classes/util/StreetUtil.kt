package cenec.mealvity.mealvity.classes.util

class StreetUtil {
    companion object {
        fun reformatedStreet(streetName: String, streetNumber: Int): String {
            val street = streetName.split(",").toMutableList()
            street.add(1, streetNumber.toString())

            var completedAddress = ""
            for (s in street) {
                completedAddress += "$s, "
            }

            return completedAddress.substring(0, completedAddress.length-2)
        }
    }
}