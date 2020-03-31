package cenec.mealvity.mealvity.classes.user

data class Address(var type: TypeOfAddress?,
                   var name: String?,
                   var number: String?,
                   var floorNumber: Byte?,
                   var door: String?,
                   var extras: String?,
                   var town: String?,
                   var postalCode: Int?) {

    constructor(): this(null, null, null, null, null, null, null, null)
}

enum class TypeOfAddress {
    CALLE, AVENIDA
}