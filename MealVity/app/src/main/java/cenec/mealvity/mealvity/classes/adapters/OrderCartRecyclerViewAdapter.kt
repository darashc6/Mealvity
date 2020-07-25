package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.orders.OrderCart
import cenec.mealvity.mealvity.classes.restaurant.menu.CartItem
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import java.util.*

/**
 * RecyclerView adapter binding the cart of an order
 * @param orderCart Cart of an order
 */
class OrderCartRecyclerViewAdapter(private var orderCart: OrderCart): RecyclerView.Adapter<OrderCartRecyclerViewAdapter.OrderCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCartViewHolder {
        return OrderCartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart_item, parent, false))
    }

    override fun onBindViewHolder(holder: OrderCartViewHolder, position: Int) {
        val list = orderCart.orderCart
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return orderCart.orderCart.size
    }

    class OrderCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvQuantity = itemView.findViewById<TextView>(R.id.text_view_quantity)
        private val tvItemName = itemView.findViewById<TextView>(R.id.text_view_item_name)
        private val tvItemPrice = itemView.findViewById<TextView>(R.id.text_view_item_price)

        /**
         * Bind the cart item to the itemview
         * @param cartItem Cart item
         */
        fun bind(cartItem: CartItem) {
            tvQuantity.text = "${cartItem.quantity}x"
            tvItemName.text = cartItem.item!!.name
            tvItemPrice.text = String.format(Locale.getDefault(), "€%.2f", (cartItem.item!!.price * cartItem.quantity))
        }
    }
}