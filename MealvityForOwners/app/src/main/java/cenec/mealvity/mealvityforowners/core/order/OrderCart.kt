package cenec.mealvity.mealvityforowners.core.order

/**
 * Class acting as a cart for an Order
 * @param orderCart List of item's
 * @param quantityTotal Total quantity of items
 * @param totalPrice Total price of the Order
 */
data class OrderCart(
    var orderCart: ArrayList<CartItem>,
    var quantityTotal: Int,
    var totalPrice: Float
) {
    constructor(): this(arrayListOf(), 0, 0f)

    /**
     * Returns price without tax
     * @return Price excluding tax
     */
    fun getBasePrice(): Float {
        return totalPrice * 0.9f
    }

    /**
     * Returns tax price
     * @return Tax price
     */
    fun getTaxPrice(): Float {
        return totalPrice - getBasePrice()
    }
}