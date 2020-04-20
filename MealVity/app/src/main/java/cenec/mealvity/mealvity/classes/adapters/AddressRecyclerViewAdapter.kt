package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.user.Address
import io.sulek.ssml.OnSwipeListener
import kotlinx.android.synthetic.main.item_list_address.view.*

/**
 * RecyclerView adapter binding the address data
 */
class AddressRecyclerViewAdapter(private var addressList: ArrayList<Address>): RecyclerView.Adapter<AddressRecyclerViewAdapter.AddressViewHolder>() {
    private lateinit var rvListener: AddressRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_address, parent, false))
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressList[position], rvListener)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    /**
     * Sets the new address list to bind
     * @param newAddressList new address list
     */
    fun setAddressList(newAddressList: ArrayList<Address>) {
        addressList = newAddressList
    }

    /**
     * Sets the listener for the RecyclerView
     * @param listener listener for the RecyclerView
     */
    fun setOnAddressRecyclerViewListener(listener: AddressRecyclerViewListener) {
        rvListener = listener
    }

    /**
     * Object containing the item's view
     * @param itemView View where we bind the data
     */
    inner class AddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val addressTitle = itemView.textView_address_title
        private val streetInfo = itemView.textView_address_street
        private val doorInfo = itemView.textView_address_door
        private val addressExtras = itemView.textView_address_extras
        private val town = itemView.textView_address_town
        private val bEditAddress = itemView.edit_address_button
        private val bDeleteAddress = itemView.delete_address_button

        /**
         * Binds the address data to the itemView
         * @param address Address containing all the data
         * @param listener Listener for the RecyclerView
         */
        fun bind(address: Address, listener: AddressRecyclerViewListener) {
            addressTitle.text = address.title
            streetInfo.text = "${address.name}, ${address.number}"
            if (address.door.equals("")) {
                doorInfo.visibility=View.GONE
            } else {
                doorInfo.text = address.door
            }
            if (address.extras.equals("")) {
                addressExtras.visibility=View.GONE
            } else {
                addressExtras.text = address.extras
            }
            town.text = "${address.town}, ${address.postalCode}"

            itemView.swipe_container.setOnSwipeListener(object : OnSwipeListener {
                override fun onSwipe(isExpanded: Boolean) {
                    address.expandedMenu = isExpanded
                }
            })

            bEditAddress.setOnClickListener {
                listener.onAddressEdit(layoutPosition)
            }

            bDeleteAddress.setOnClickListener {
                listener.onAddressDelete(layoutPosition)
            }

            itemView.swipe_container.apply(address.expandedMenu)
        }
    }

    /**
     * Interface for the RecyclerView
     */
    interface AddressRecyclerViewListener {
        /**
         * Deletes the address from the list
         * @param position Index of the list
         */
        fun onAddressDelete(position: Int)

        /**
         * Edits an address from the list
         * @param position Index of the list
         */
        fun onAddressEdit(position: Int)
    }
}