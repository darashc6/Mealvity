package cenec.mealvity.mealvity.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentHomeTabBinding
import cenec.mealvity.mealvity.activities.AutocompleteStreetActivity
import cenec.mealvity.mealvity.activities.ShowRestaurantListingActivity
import cenec.mealvity.mealvity.classes.adapters.CuisineRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.config.SharedPreferencesConfig
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.restaurant.Cuisine
import cenec.mealvity.mealvity.classes.singleton.StreetSingleton
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import java.util.*
import kotlin.collections.ArrayList

/**
 * Fragment of the Home Tab
 */
class HomeTabFragment : Fragment() {
    private var _binding: FragmentHomeTabBinding? = null // View binding of the fragment
    private val binding = _binding!! // Non-nullable version of the binding variable above
    private lateinit var categoryList: ArrayList<Cuisine> // List of categories
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // User currently logged in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.textViewUser.text = "Hi, ${userLoggedIn.fullName}!"

        binding.editTextAddress.background = null
        binding.editTextAddress.setText(SharedPreferencesConfig(context!!).getDefaultStreet())
        binding.editTextAddress.setOnClickListener {
            val intentAutocompleteStreet = Intent(context, AutocompleteStreetActivity::class.java)
            startActivity(intentAutocompleteStreet)
        }

        binding.searchRestaurantListings.setOnClickListener {
            val address = binding.editTextAddress.text.toString()
            if (address.isEmpty()) {
                binding.editTextAddress.error = getString(R.string.text_field_empty)
                binding.editTextAddress.requestFocus()
            } else {
                val intentGetBusinessListings = Intent(context, ShowRestaurantListingActivity::class.java)
                val bun = Bundle()
                bun.putString(BundleKeys.ADDRESS_SEARCH, address)
                intentGetBusinessListings.putExtras(bun)
                startActivity(intentGetBusinessListings)
            }
        }

        setupCategoryList()
        setupRecyclerView()

        StreetSingleton.setStreetSingletonListener(object : StreetSingleton.StreetSingletonListener{
            override fun onStreetSelectedListener(streetSelected: String) {
                binding.editTextAddress.setText(streetSelected)
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /**
     * Sets up a category list to filter restaurants
     */
    private fun setupCategoryList() {
        categoryList = arrayListOf()

        categoryList.add(Cuisine("Bars", context!!.resources.getIdentifier("bars", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Burgers", context!!.resources.getIdentifier("burgers", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Chinese", context!!.resources.getIdentifier("chinese", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Indian", context!!.resources.getIdentifier("indian", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Italian", context!!.resources.getIdentifier("italian", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Japanese", context!!.resources.getIdentifier("japanese", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Mexican", context!!.resources.getIdentifier("mexican", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Pizza", context!!.resources.getIdentifier("pizza", "drawable", context!!.packageName)))
        categoryList.add(Cuisine("Spanish", context!!.resources.getIdentifier("spanish", "drawable", context!!.packageName)))
    }

    /**
     * Sets up a RecyclerView containing a list of categories
     */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.stackFromEnd = false
        binding.recyclerViewCuisinesList.layoutManager = layoutManager

        val adapter = CuisineRecyclerViewAdapter(categoryList)
        adapter.setCategoryRecyclerViewListener(object : CuisineRecyclerViewAdapter.CuisineRecyclerViewListener{
            override fun onClick(position: Int) {
                val category = when (position) {
                    3 -> "indpak"
                    else -> categoryList[position].name.toLowerCase(Locale.ROOT)
                }
                val address = binding.editTextAddress.text.toString()

                val intentGetBusinessListing = Intent(context, ShowRestaurantListingActivity::class.java)
                intentGetBusinessListing.putExtra(BundleKeys.RESTAURANT_CATEGORY, category)
                intentGetBusinessListing.putExtra(BundleKeys.ADDRESS_SEARCH, address)
                startActivity(intentGetBusinessListing)
            }

        })
        binding.recyclerViewCuisinesList.adapter = adapter
    }

}
