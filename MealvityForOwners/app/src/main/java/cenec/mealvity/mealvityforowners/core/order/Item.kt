package cenec.mealvity.mealvityforowners.core.order


data class Item(
    var name: String,
    var description: String,
    var price: Float
)

data class CartItem(
    var item: Item?,
    var quantity: Int
)