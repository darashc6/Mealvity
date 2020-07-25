package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ItemListOrderBinding
import cenec.mealvity.mealvity.classes.user.Order
import java.util.*
import kotlin.collections.ArrayList

/**
 * RecyclerView adapter binding a list of orders
 * @param orderList List of orders
 */
class OrderRecyclerViewAdapter(private var orderList: ArrayList<Order>): RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder>() {
    private var rvListener: OrderRecyclerViewListener? = null // Listener for the RecyclerView adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    /**
     * Sets the listener for the RecyclerView
     * @param newListener New listener for the RecyclerView
     */
    fun setOrderRecyclerViewListener (newListener: OrderRecyclerViewListener) {
        rvListener = newListener
    }

    /**
     * Sets a new list of order for the RecyclerView
     * @param newOrderList New order list
     */
    fun setOrderList(newOrderList: ArrayList<Order>) {
        orderList = newOrderList
        notifyDataSetChanged()
    }

    inner class OrderViewHolder(_binding: ItemListOrderBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        /**
         * Binds an Order to the itemview
         * @param order Order
         */
        fun bind(order: Order) {
            binding.textViewReferenceNumber.text = "Reference Nº: ${order.referenceNumber}"
            binding.textViewRestaurantName.text = "Restaurant: ${order.restaurantName}"
            binding.textViewOrderPrice.text = String.format(Locale.getDefault(), "Total: €%.2f", order.orderCart!!.totalPrice)
            binding.textViewPaymentMethod.text = "Payment: ${order.paymentMethod}"
            binding.textViewDeliveryMethod.text = "Delivery: ${order.deliveryMode}"
            binding.textViewStatus.text = "${order.orderStatus}"

            if (order.orderStatus == Order.OrderStatus.ACCEPTED) {
                binding.textViewStatus.setTextColor(binding.root.context.getColor(R.color.green_accept))
            } else if (order.orderStatus == Order.OrderStatus.REJECTED) {
                binding.textViewStatus.setTextColor(binding.root.context.getColor(R.color.red_reject))
            }

            binding.textViewMoreInfo.setOnClickListener {
                rvListener?.onItemClick(layoutPosition)
            }
        }
    }

    /**
     * Interface for the RecyclerView
     */
    interface OrderRecyclerViewListener {
        /**
         * Executes when an item is clicked
         * @param position Item's position
         */
        fun onItemClick(position: Int)
    }
}