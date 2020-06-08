package cenec.mealvity.mealvity.classes.orders

import android.os.Parcelable
import cenec.mealvity.mealvity.classes.restaurant.menu.CartItem
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import java.io.Serializable


class OrderCart(
    var orderCart: ArrayList<CartItem> = arrayListOf(),
    var quantityTotal: Int = 0,
    var totalPrice: Float = 0f
): Serializable {
    @Transient
    private lateinit var oListener: OrderListener

    fun setOrderListener(listener: OrderListener) {
        oListener = listener
    }

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



    fun getBasePrice(): Float {
        return totalPrice * 0.79f
    }

    fun getTaxPrice(): Float {
        return totalPrice - getBasePrice()
    }

}

interface OrderListener {
    fun onOrderCartUpdated()
}