package cenec.mealvity.mealvityforowners.core.user



data class Address(var title: String?,
                   var name: String?,
                   var number: String?,
                   var door: String?,
                   var extras: String?,
                   var town: String?,
                   var postalCode: String?,
                   var expandedMenu: Boolean = false) {

    constructor(): this(null, null, null, null, null, null, null)
}