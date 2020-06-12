package cenec.mealvity.mealvityforowners.core.order

data class OrderCart(
    var orderCart: ArrayList<CartItem>,
    var quantityTotal: Int,
    var totalPrice: Float
) {
    constructor(): this(arrayListOf(), 0, 0f)

    fun getBasePrice(): Float {
        return totalPrice * 0.79f
    }

    fun getTaxPrice(): Float {
        return totalPrice - getBasePrice()
    }
}