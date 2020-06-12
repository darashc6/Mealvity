package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ItemListOrderBinding
import cenec.mealvity.mealvity.classes.user.Order
import java.util.*
import kotlin.collections.ArrayList

class OrderRecyclerViewAdapter(private var orderList: ArrayList<Order>): RecyclerView.Adapter<OrderRecyclerViewAdapter.OrderViewHolder>() {
    private var rvListener: OrderRecyclerViewListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orderList[position], rvListener)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun setOrderRecyclerViewListener (newListener: OrderRecyclerViewListener) {
        rvListener = newListener
    }

    fun setOrderList(newOrderList: ArrayList<Order>) {
        orderList = newOrderList
        notifyDataSetChanged()
    }

    class OrderViewHolder(_binding: ItemListOrderBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        fun bind(order: Order, listener: OrderRecyclerViewListener?) {
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
                listener?.onItemClick(layoutPosition)
            }
        }
    }

    interface OrderRecyclerViewListener {
        fun onItemClick(position: Int)
    }
}