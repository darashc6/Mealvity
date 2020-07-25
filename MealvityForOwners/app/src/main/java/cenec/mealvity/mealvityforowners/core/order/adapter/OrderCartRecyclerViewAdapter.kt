package cenec.mealvity.mealvityforowners.core.order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.mealvity.mealvityforowners.core.order.CartItem
import cenec.mealvity.mealvityforowners.databinding.ItemListCartItemBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * RecyclerView adapter binding the cart of an order
 * @param orderCart Cart of an order
 */
class OrderCartRecyclerViewAdapter(private var orderCart: ArrayList<CartItem>): RecyclerView.Adapter<OrderCartRecyclerViewAdapter.CartItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemListCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(orderCart[position])
    }

    override fun getItemCount(): Int {
        return orderCart.size
    }

    class CartItemViewHolder(_binding: ItemListCartItemBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        /**
         * Bind the cart item to the itemview
         * @param cartItem Cart item
         */
        fun bind(cartItem: CartItem) {
            binding.textViewQuantity.text = "${cartItem.quantity}x"
            binding.textViewItemName.text = cartItem.item!!.name
            binding.textViewItemPrice.text = String.format(Locale.getDefault(), "€%.2f", cartItem.item!!.price)
        }
    }
}