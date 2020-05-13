package cenec.mealvity.mealvity.classes.singleton

/**
 * Static class for storing a street string
 * Used for communicating between Activities
 */
class StreetSingleton {
    companion object {
        private lateinit var defaultStreet: String // Street name
        private lateinit var listener: StreetSingletonListener // Listener for the class

        /**
         * Sets the new street
         * @param newStreet new street to store in the class
         */
        fun setStreet(newStreet: String) {
            defaultStreet = newStreet
            listener.onStreetSelectedListener(defaultStreet)
        }

        /**
         * Returns the street stored in this class
         * @return String with the street name
         */
        fun getStreet(): String {
            return if (defaultStreet.isEmpty()){
                ""
            } else {
                defaultStreet
            }
        }

        /**
         * Sets the listener for the class
         * @param newListener New listener for the class
         */
        fun setStreetSingletonListener(newListener: StreetSingletonListener) {
            listener = newListener
        }
    }

    /**
     * Interface of the class
     */
    interface StreetSingletonListener {
        /**
         * When the user selects a street from the suggestions in the AutocompleteStreetActivity
         * @param streetSelected String of the street selected
         */
        fun onStreetSelectedListener(streetSelected: String)
    }
}