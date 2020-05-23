package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.databinding.ItemListRestaurantBinding
import cenec.mealvity.mealvity.classes.restaurant.Restaurant
import cenec.mealvity.mealvity.classes.restaurant.RestaurantList
import com.bumptech.glide.Glide
import kotlin.math.floor

/**
 * RecyclerView adapter binding the RestaurantList data
 * @param restaurantList List of Restaurant
 */
class RestaurantRecyclerViewAdapter(private var restaurantList: RestaurantList): RecyclerView.Adapter<RestaurantRecyclerViewAdapter.RestaurantViewHolder>() {
    private lateinit var _binding: ItemListRestaurantBinding // Binding of the layout used for this RecyclerView
    private lateinit var rvListener: RestaurantRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        _binding = ItemListRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurantList.results[position], rvListener)
    }

    override fun getItemCount(): Int {
        return restaurantList.results.size
    }

    fun setRestaurantRecyclerViewListener(listener: RestaurantRecyclerViewListener) {
        rvListener = listener
    }

    /**
     * Sets a new list of Restaurant
     * @param newRestaurantList New list
     */
    fun setRestaurantList(newRestaurantList: RestaurantList) {
        restaurantList = newRestaurantList
    }

    class RestaurantViewHolder(_binding: ItemListRestaurantBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        /**
         * Binds the Restaurant data to the itemView
         * @param restaurant Restaurant containing all its data
         */
        fun bind(restaurant: Restaurant, listener: RestaurantRecyclerViewListener) {
            if (!restaurant.image_url.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(restaurant.image_url)
                    .centerCrop()
                    .into(binding.imageViewRestaurant)
            }
            binding.restaurantName.text = restaurant.name
            binding.restaurantCategory.text = restaurant.displayCategories()
            binding.restaurantPriceRange.text = "Price range: ${restaurant.price}"
            binding.restaurantAddress.text = restaurant.location.address
            binding.restaurantPhone.text = restaurant.phone
            binding.restaurantDistance.text = "${floor(restaurant.distance).toInt()} m"
            binding.restaurantRatings.rating = restaurant.rating
            binding.restaurantMoreInfo.setOnClickListener {
                listener.onMoreInfoClick(restaurant.id)
            }
        }
    }

    interface RestaurantRecyclerViewListener {
        fun onMoreInfoClick(restaurantId: String)
    }
}