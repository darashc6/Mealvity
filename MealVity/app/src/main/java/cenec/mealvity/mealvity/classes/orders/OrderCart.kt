package cenec.mealvity.mealvity.classes.orders


import cenec.mealvity.mealvity.classes.restaurant.menu.CartItem
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import java.io.Serializable

/**
 * Class acting as a cart for an Order
 * @param orderCart List of item's
 * @param quantityTotal Total quantity of items
 * @param totalPrice Total price of the Order
 */
class OrderCart(
    var orderCart: ArrayList<CartItem> = arrayListOf(),
    var quantityTotal: Int = 0,
    var totalPrice: Float = 0f
): Serializable {
    @Transient
    private lateinit var oListener: OrderListener // Listener for the class

    /**
     * Sets the listener for the class
     * @param listener New listener
     */
    fun setOrderListener(listener: OrderListener) {
        oListener = listener
    }

    /**
     * Adds a new item to the order cart
     * @param menuItem Item to add
     * @param quantity Item quantity
     */
    fun addItemToOrder(menuItem: Item, quantity: Int) {
        var isItemDuplicate = false
        var position = 0

        if (orderCart.isNotEmpty()) {
            for (i in 0 until orderCart.size) {
                if (orderCart[i].item == menuItem) {
                    isItemDuplicate = true
                    position = i
                }
            }
        }

        if (!isItemDuplicate) {
            orderCart.add(CartItem(menuItem, quantity))
        } else {
            val previousQuantity = orderCart[position].quantity
            orderCart[position].quantity = previousQuantity + quantity
        }

        quantityTotal += quantity
        totalPrice += (menuItem.price * quantity)

        oListener.onOrderCartUpdated()
    }

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

    /**
     * Empties the order cart, resetting to it's initial values
     */
    fun emptyOrderCart() {
        orderCart.clear()
        quantityTotal = 0
        totalPrice = 0f
    }

}

/**
 * Listener for the class
 */
interface OrderListener {
    /**
     * Triggered whenever the cart is updated
     */
    fun onOrderCartUpdated()
}