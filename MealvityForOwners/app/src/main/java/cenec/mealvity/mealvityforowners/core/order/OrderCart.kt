package cenec.mealvity.mealvityforowners.core.order

data class OrderCart(
    var orderCart: ArrayList<CartItem>,
    var quantityTotal: Int,
    var totalPrice: Float
)