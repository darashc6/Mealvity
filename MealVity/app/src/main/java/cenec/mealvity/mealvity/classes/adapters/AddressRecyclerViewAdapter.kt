package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ItemListAddressBinding
import cenec.mealvity.mealvity.classes.user.Address
import io.sulek.ssml.OnSwipeListener
import kotlinx.android.synthetic.main.item_list_address.view.*

/**
 * RecyclerView adapter binding the address data
 * @param addressList List of Address
 */
class AddressRecyclerViewAdapter(private var addressList: ArrayList<Address>): RecyclerView.Adapter<AddressRecyclerViewAdapter.AddressViewHolder>() {
    private lateinit var rvListener: AddressRecyclerViewListener // Listener for the RecyclerView adapter
    private lateinit var _binding: ItemListAddressBinding // View binding of the layout used for this RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        _binding = ItemListAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(_binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    /**
     * Sets a new address list for the adapter
     * @param newAddressList New address list
     */
    fun setAddressList(newAddressList: ArrayList<Address>) {
        addressList = newAddressList
        notifyDataSetChanged()
    }

    /**
     * Sets the listener for the RecyclerView
     * @param listener listener for the RecyclerView
     */
    fun setOnAddressRecyclerViewListener(listener: AddressRecyclerViewListener) {
        rvListener = listener
    }


    inner class AddressViewHolder(_binding: ItemListAddressBinding): RecyclerView.ViewHolder(_binding.root) {
        private val binding = _binding

        /**
         * Binds the address data to the itemView
         * @param address Address containing all the data
         */
        fun bind(address: Address) {
            binding.textViewAddressTitle.text = address.title
            binding.textViewAddressStreet.text = "${address.name}, ${address.number}"
            if (address.door.equals("")) {
                binding.textViewAddressDoor.visibility=View.GONE
            } else {
                binding.textViewAddressDoor.text = address.door
            }
            if (address.extras.equals("")) {
                binding.textViewAddressExtras.visibility=View.GONE
            } else {
                binding.textViewAddressExtras.text = address.extras
            }
            binding.textViewAddressTown.text = "${address.town}, ${address.postalCode}"

            itemView.swipe_container.setOnSwipeListener(object : OnSwipeListener {
                override fun onSwipe(isExpanded: Boolean) {
                    address.expandedMenu = isExpanded
                }
            })

            binding.editAddressButton.setOnClickListener {
                rvListener.onAddressEdit(layoutPosition)
            }

            binding.deleteAddressButton.setOnClickListener {
                rvListener.onAddressDelete(layoutPosition)
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