package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.databinding.ItemListRestaurantBinding
import cenec.mealvity.mealvity.classes.restaurant.Restaurant
import com.bumptech.glide.Glide
import kotlin.math.floor

class RestaurantRecyclerViewAdapter(private var listRestaurant: ArrayList<Restaurant>): RecyclerView.Adapter<RestaurantRecyclerViewAdapter.RestaurantViewHolder>() {
    private lateinit var binding: ItemListRestaurantBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        binding = ItemListRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(listRestaurant[position])
    }

    override fun getItemCount(): Int {
        return listRestaurant.size
    }

    class RestaurantViewHolder(binding: ItemListRestaurantBinding): RecyclerView.ViewHolder(binding.root) {
        private val _binding = binding

        fun bind(restaurant: Restaurant) {
            if (!restaurant.image_url.isNullOrEmpty()) {
                Glide.with(_binding.root)
                    .load(restaurant.image_url)
                    .centerCrop()
                    .into(_binding.imageViewRestaurant)
            }
            _binding.restaurantName.text = restaurant.name
            _binding.restaurantCategory.text = restaurant.displayCategories()
            if (!restaurant.price.isNullOrEmpty()) {
                _binding.restaurantPriceRange.text = "Price range: ${restaurant.price}"
            } else {
                _binding.restaurantPriceRange.text = "Price range: €€ - €€€"
            }
            _binding.restaurantAddress.text = restaurant.location.address
            _binding.restaurantPhone.text = restaurant.phone
            _binding.restaurantDistance.text = "${floor(restaurant.distance).toInt().toString()} m"
            _binding.restaurantRatings.rating = restaurant.rating
        }
    }
}