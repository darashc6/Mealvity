package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetName

/**
 * RecyclerView adapter binding the StreetList data
 * @param streetList List of Street
 */
class AutocompleteStreetRecyclerViewAdapter(private var streetList: StreetList): RecyclerView.Adapter<AutocompleteStreetRecyclerViewAdapter.AutocompleteStreetViewHolder>() {
    private lateinit var rvListener: AutocompleteStreetRecyclerViewListener // Listener for the recyclerView

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutocompleteStreetViewHolder {
        return AutocompleteStreetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_autocomplete, parent, false))
    }

    override fun onBindViewHolder(holder: AutocompleteStreetViewHolder, position: Int) {
        holder.bind(streetList.results[position], rvListener)
    }

    override fun getItemCount(): Int {
        return streetList.results.size
    }

    /**
     * Sets the listener for the RecyclerView
     * @param listener Listener for the RecyclerView
     */
    fun setAutocompleteStreetRecyclerViewListener(listener: AutocompleteStreetRecyclerViewListener) {
        rvListener = listener
    }

    /**
     * Sets the new StreetList data
     * @param newStreetList New StreetList list
     */
    fun setStreetList (newStreetList: StreetList) {
        streetList = newStreetList
    }

    /**
     * Object containing the item's view
     * @param itemView View where we bind the data
     */
    class AutocompleteStreetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val streetName = itemView.findViewById<TextView>(R.id.text_view_street_name)

        /**
         * Binds the street data to the itemView
         * @param name Street Name
         * @param listener Listener for the RecyclerView
         */
        fun bind (name: StreetName, listener: AutocompleteStreetRecyclerViewListener) {
            streetName.text = name.streetName
            itemView.setOnClickListener {
                listener.onClick(name.streetName)
            }
        }
    }

    /**
     * Interface for the RecyclerView
     */
    interface AutocompleteStreetRecyclerViewListener {
        /**
         * Click for the itemView
         * @param streetName Street name selected from the list
         */
        fun onClick(streetName: String)
    }
}