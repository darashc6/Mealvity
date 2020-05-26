package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.menu.Item

class SectionRecyclerViewAdapter(private val items: ArrayList<Item>): RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder>() {
    private lateinit var rvListener: SectionRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        return SectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(items, position, rvListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setSectionRecyclerViewListener(listener: SectionRecyclerViewListener) {
        rvListener = listener
    }

    class SectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvItemName = itemView.findViewById<TextView>(R.id.text_view_item_name)
        private val tvItemDescription = itemView.findViewById<TextView>(R.id.text_view_item_description)
        private val tvItemPrice = itemView.findViewById<TextView>(R.id.text_view_item_price)

        fun bind(list: ArrayList<Item>, position: Int, listener: SectionRecyclerViewListener) {
            tvItemName.text = list[position].name
            tvItemDescription.text = list[position].description
            tvItemPrice.text = "€${list[position].price}"

            itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }
    }

    interface SectionRecyclerViewListener {
        fun onItemClick(itemPosition: Int)
    }
}