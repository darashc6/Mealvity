package cenec.mealvity.mealvity.classes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.user.Address

class UserAddressRecyclerViewAdapter(private var userAddressList: ArrayList<Address>): RecyclerView.Adapter<UserAddressRecyclerViewAdapter.UserAddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAddressViewHolder {
        return UserAddressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_radio_button_address, parent, false))
    }

    override fun onBindViewHolder(holder: UserAddressViewHolder, position: Int) {
        holder.bind(userAddressList[position])
    }

    override fun getItemCount(): Int {
        return userAddressList.size
    }


    class UserAddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rbAddress = itemView.findViewById<RadioButton>(R.id.radio_button_address)

        fun bind(userAddress: Address) {
            rbAddress.text = userAddress.name
        }
    }
}