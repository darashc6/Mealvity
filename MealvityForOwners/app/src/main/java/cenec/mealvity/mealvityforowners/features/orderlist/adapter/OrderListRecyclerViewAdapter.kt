package cenec.mealvity.mealvityforowners.features.orderlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.mealvity.mealvityforowners.R
import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.databinding.ItemListOrderBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * RecyclerView adapter binding a list of orders
 * @param orderList List of orders
 */
class OrderListRecyclerViewAdapter(private var orderList: ArrayList<Order>): RecyclerView.Adapter<OrderListRecyclerViewAdapter.OrderViewHolder>() {
    private var rvListener: OrderListRecyclerViewListener? = null // Listener for the RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val _binding = ItemListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position], rvListener)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    /**
     * Sets the listener for the RecyclerView
     * @param newListener New listener for the RecyclerView
     */
    fun setOrderListRecyclerViewListener (newListener: OrderListRecyclerViewListener) {
        rvListener = newListener
    }

    /**
     * Sets a new list of order for the RecyclerView
     * @param newOrderList New order list
     */
    fun setOrderList (newOrderList: ArrayList<Order>) {
        orderList = newOrderList
        notifyDataSetChanged()
    }

    class OrderViewHolder(_binding: ItemListOrderBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        /**
         * Binds an Order to the itemview
         * @param order Order
         */
        fun bind(order: Order, listener: OrderListRecyclerViewListener?) {
            binding.textViewReferenceNumber.text = "Reference Nº: ${order.referenceNumber}"
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
                listener?.onItemClick(layoutPosition)
            }
        }
    }

    interface OrderListRecyclerViewListener {
        fun onItemClick(position: Int)
    }
}