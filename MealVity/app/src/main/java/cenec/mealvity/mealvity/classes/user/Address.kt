package cenec.mealvity.mealvity.classes.user

/**
 * Class acting as an address
 * @param title Title of the address
 * @param name Street name
 * @param number Street number
 * @param door Door info (Floor number, door name, etc)
 * @param extras Address extras (Apartment name, doorbell, etc)
 * @param town Town of the address
 * @param postalCode Postal code of the address
 * @param expandedMenu Menu to delete or edit an dddress. True if it is expanded, false if otherwise
 */
data class Address(var title: String?,
                   var name: String?,
                   var number: String?,
                   var door: String?,
                   var extras: String?,
                   var town: String?,
                   var postalCode: String?,
                   var expandedMenu:Boolean = false) {

    constructor(): this(null, null, null, null, null, null, null)
}