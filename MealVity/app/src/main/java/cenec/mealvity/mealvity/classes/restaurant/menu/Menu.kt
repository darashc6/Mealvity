package cenec.mealvity.mealvity.classes.restaurant.menu


import java.io.Serializable

data class Menu(
    var menu: ArrayList<Section>
)

data class Section(
    var name: String,
    var items: ArrayList<Item>,
    var isExpanded: Boolean
)

data class Item(
    var name: String,
    var description: String,
    var price: Float
): Serializable {
    constructor(): this("", "", 0f)
}

data class CartItem(
    var item: Item?,
    var quantity: Int
): Serializable {
    constructor(): this(null, 0)
}