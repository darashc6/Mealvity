package cenec.mealvity.mealvityforowners.core.order


data class Item(
    var name: String,
    var description: String,
    var price: Float
) {
    constructor(): this("", "", 0f)
}

data class CartItem(
    var item: Item?,
    var quantity: Int
) {
    constructor(): this(null, 0)
}