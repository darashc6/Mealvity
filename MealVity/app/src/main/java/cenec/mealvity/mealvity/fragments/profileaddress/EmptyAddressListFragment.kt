package cenec.mealvity.mealvity.fragments.profileaddress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.R

/**
 * Fragment containing an emty list of addresses
 */
class EmptyAddressListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_address_list, container, false)
    }

}
