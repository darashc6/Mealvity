package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.Categories
import cenec.mealvity.mealvity.classes.restaurant.Cuisine
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list_category.view.*

/**
 * RecyclerView adapter binding the Cuisine data
 * @param list List of Cuisine
 */
class CuisineRecyclerViewAdapter(var list: ArrayList<Cuisine>): RecyclerView.Adapter<CuisineRecyclerViewAdapter.CuisineViewHolder>() {
    private lateinit var mListener: CuisineRecyclerViewListener // Listener for the RecyclerView adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineViewHolder {
        return CuisineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_category, parent, false))
    }

    override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * Sets the listener for the RecyclerView
     * @param listener listener for the RecyclerView
     */
    fun setCategoryRecyclerViewListener(listener: CuisineRecyclerViewListener) {
        mListener = listener
    }

    inner class CuisineViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameCuisine = itemView.text_cuisine_name
        private val imageCuisine = itemView.image_view_cuisine

        /**
         * Binds the address data to the itemView
         * @param cuisine Cuisine containing all the data
         * @param listener Listener for the RecyclerView
         */
        fun bind(cuisine: Cuisine) {
            nameCuisine.text = cuisine.name
            Glide.with(itemView)
                .load(cuisine.resourceId)
                .into(imageCuisine)
            itemView.setOnClickListener {
                mListener.onClick(layoutPosition)
            }
        }
    }

    /**
     * Interface for the RecyclerView
     */
    interface CuisineRecyclerViewListener {
        /**
         * OnClick for the itemView
         * @param position Index of the list
         */
        fun onClick(position: Int)
    }
}