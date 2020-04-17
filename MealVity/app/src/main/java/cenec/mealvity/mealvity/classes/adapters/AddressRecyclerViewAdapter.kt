package cenec.mealvity.mealvity.classes.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.user.Address
import io.sulek.ssml.OnSwipeListener
import kotlinx.android.synthetic.main.item_list_address.view.*

class AddressRecyclerViewAdapter(private var addressList: ArrayList<Address>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var rvListener: AddressRecyclerViewListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_address, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddressViewHolder -> {
                holder.bind(addressList[position], rvListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun setAddressList(newAddressList: ArrayList<Address>) {
        addressList = newAddressList
    }

    fun setOnAddressRecyclerViewListener(listener: AddressRecyclerViewListener) {
        rvListener = listener
    }

    inner class AddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val addressTitle = itemView.textView_address_title
        private val streetInfo = itemView.textView_address_street
        private val doorInfo = itemView.textView_address_door
        private val addressExtras = itemView.textView_address_extras
        private val town = itemView.textView_address_town
        private val bEditAddress = itemView.edit_address_button
        private val bDeleteAddress = itemView.delete_address_button

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

    interface AddressRecyclerViewListener {
        fun onAddressDelete(position: Int)
        fun onAddressEdit(position: Int)
    }
}