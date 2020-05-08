package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetName

class AutocompleteStreetRecyclerViewAdapter(var list: StreetList): RecyclerView.Adapter<AutocompleteStreetRecyclerViewAdapter.AutocompleteStreetViewHolder>() {
    private lateinit var rvListener: AutocompleteStreetRecyclerViewListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutocompleteStreetViewHolder {
        return AutocompleteStreetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_autocomplete, parent, false))
    }

    override fun onBindViewHolder(holder: AutocompleteStreetViewHolder, position: Int) {
        holder.bind(list.streetList[position], rvListener)
    }

    override fun getItemCount(): Int {
        return list.streetList.size
    }

    fun setAutocompleteStreetRecyclerViewListener(listener: AutocompleteStreetRecyclerViewListener) {
        rvListener = listener
    }

    class AutocompleteStreetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val streetName = itemView.findViewById<TextView>(R.id.text_view_street_name)

        fun bind (name: StreetName, listener: AutocompleteStreetRecyclerViewListener) {
            streetName.text = name.streetName
            itemView.setOnClickListener {
                listener.onClick(layoutPosition)
            }
        }
    }

    interface AutocompleteStreetRecyclerViewListener {
        fun onClick(position: Int)
    }
}