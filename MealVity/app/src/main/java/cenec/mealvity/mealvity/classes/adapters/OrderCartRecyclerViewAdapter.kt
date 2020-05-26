package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.orders.Order
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import java.util.*

class OrderCartRecyclerViewAdapter(private var order: Order): RecyclerView.Adapter<OrderCartRecyclerViewAdapter.OrderCartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCartViewHolder {
        return OrderCartViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_cart_item, parent, false))
    }

    override fun onBindViewHolder(holder: OrderCartViewHolder, position: Int) {
        val list = order.orderCart.toList()
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return order.orderCart.size
    }

    class OrderCartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvQuantity = itemView.findViewById<TextView>(R.id.text_view_quantity)
        private val tvItemName = itemView.findViewById<TextView>(R.id.text_view_item_name)
        private val tvItemPrice = itemView.findViewById<TextView>(R.id.text_view_item_price)

        fun bind(pair: Pair<Item, Int>) {
            tvQuantity.text = "${pair.second}x"
            tvItemName.text = pair.first.name
            tvItemPrice.text = String.format(Locale.getDefault(), "€%.2f", (pair.second * pair.first.price))
        }
    }
}