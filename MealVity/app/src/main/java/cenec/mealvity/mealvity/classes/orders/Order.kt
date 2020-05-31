package cenec.mealvity.mealvity.classes.orders

import android.os.Parcel
import android.os.Parcelable
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import java.io.Serializable


class Order(
    var orderCart: HashMap<Item, Int> = hashMapOf(),
    var quantityTotal: Int = 0,
    var totalPrice: Float = 0f
): Serializable {
    @Transient
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