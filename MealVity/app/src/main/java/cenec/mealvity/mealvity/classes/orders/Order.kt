package cenec.mealvity.mealvity.classes.orders

import cenec.mealvity.mealvity.classes.restaurant.menu.Item

class Order(
    var orderCart: HashMap<Item, Int> = hashMapOf(),
    var quantityTotal: Int = 0,
    var totalPrice: Float = 0f
) {
    private lateinit var oListener: OrderListener

    fun setOrderListener(listener: OrderListener) {
        oListener = listener
    }

    fun addItemToOrder(menuItem: Item, quantity: Int) {
        if (!orderCart.containsKey(menuItem)) {
            orderCart[menuItem] = quantity
            quantityTotal += quantity
            totalPrice += (menuItem.price * quantity)
        } else {
            val previousQuantity = orderCart[menuItem]
            orderCart[menuItem] = previousQuantity!! + quantity
            quantityTotal += quantity
            totalPrice += (menuItem.price * quantity)
        }

        oListener.onOrderCartUpdated()
    }

}

interface OrderListener {
    fun onOrderCartUpdated()
}