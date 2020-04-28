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

class CuisineRecyclerViewAdapter(var list: ArrayList<Cuisine>): RecyclerView.Adapter<CuisineRecyclerViewAdapter.CuisineViewHolder>() {
    private lateinit var mListener: CategoryRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineViewHolder {
        return CuisineViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_category, parent, false))
    }

    override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
        holder.bind(list[position], mListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setCategoryRecyclerViewListener(listener: CategoryRecyclerViewListener) {
        mListener = listener
    }

    class CuisineViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val nameCuisine = itemView.findViewById<TextView>(R.id.text_cuisine_name)
        private val imageCuisine = itemView.findViewById<ImageView>(R.id.image_view_cuisine)

        fun bind(cuisine: Cuisine, listener: CategoryRecyclerViewListener) {
            nameCuisine.text = cuisine.name
            Glide.with(itemView)
                .load(cuisine.resourceId)
                .into(imageCuisine)
            itemView.setOnClickListener {
                listener.onClick(layoutPosition)
            }
        }
    }

    interface CategoryRecyclerViewListener {
        fun onClick(position: Int)
    }
}