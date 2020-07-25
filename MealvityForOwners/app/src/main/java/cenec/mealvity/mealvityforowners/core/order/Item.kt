package cenec.mealvity.mealvityforowners.core.order


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
) {
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
) {
    constructor(): this(null, 0)
}