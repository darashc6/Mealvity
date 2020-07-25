package cenec.mealvity.mealvity.classes.restaurant.menu


import java.io.Serializable

/**
 * Class acting as a restaurant menu
 * @param menu List of sections in a menu
 */
data class Menu(
    var menu: ArrayList<Section>
)

/**
 * Class acting as a section in the menu
 * @param name Section's name
 * @param items List of items in the section
 * @param isExpanded True if the list is expanded, false if otherwise
 */
data class Section(
    var name: String,
    var items: ArrayList<Item>,
    var isExpanded: Boolean
)

/**
 * Class acting as an item in the menu
 * @param name Name of the item
 * @param description Description of the item
 * @param price Price of the item
 */
data class Item(
    var name: String,
    var description: String,
    var price: Float
): Serializable {
    constructor(): this("", "", 0f)
}

/**
 * Class acting as an item in the cart
 * @param item Item
 * @param quantity Quantity of the item
 */
data class CartItem(
    var item: Item?,
    var quantity: Int
): Serializable {
    constructor(): this(null, 0)
}