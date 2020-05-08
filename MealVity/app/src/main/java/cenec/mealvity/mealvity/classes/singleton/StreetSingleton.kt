package cenec.mealvity.mealvity.classes.singleton

class StreetSingleton {
    companion object {
        private lateinit var defaultStreet: String
        private lateinit var listener: StreetSingletonListener

        fun setStreet(newAddress: String) {
            defaultStreet = newAddress
            listener.onStreetSelectedListener(defaultStreet)
        }

        fun getStreet(): String {
            return if (defaultStreet.isEmpty()){
                ""
            } else {
                defaultStreet
            }
        }

        fun setStreetSingletonListener(newListener: StreetSingletonListener) {
            listener = newListener
        }
    }

    interface StreetSingletonListener {
        fun onStreetSelectedListener(streetSelected: String)
    }
}