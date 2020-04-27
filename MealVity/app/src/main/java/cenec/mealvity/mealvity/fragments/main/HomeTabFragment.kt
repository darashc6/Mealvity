package cenec.mealvity.mealvity.fragments.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentHomeTabBinding
import cenec.mealvity.mealvity.activities.ShowRestaurantListing
import cenec.mealvity.mealvity.classes.user.User

/**
 * Fragment of the Home Tab
 */
class HomeTabFragment : Fragment() {
    private var _binding: FragmentHomeTabBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeTabBinding.inflate(inflater, container, false)
        val view = binding!!.root

        binding!!.searchRestautantListings.setOnClickListener {
            val address = binding!!.editTextAddress.text.toString()
            if (address.isEmpty()) {
                binding!!.editTextAddress.error = getString(R.string.text_field_empty)
                binding!!.editTextAddress.requestFocus()
            } else {
                val intentGetBusinessListings = Intent(context, ShowRestaurantListing::class.java)
                val bun = Bundle()
                bun.putString("address_search", address)
                intentGetBusinessListings.putExtras(bun)
                startActivity(intentGetBusinessListings)
            }
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
