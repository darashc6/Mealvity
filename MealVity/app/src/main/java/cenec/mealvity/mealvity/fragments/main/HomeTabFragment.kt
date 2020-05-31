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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
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
    private lateinit var fragmentView: View
    private lateinit var rvCuisines: RecyclerView
    private lateinit var tvUser: TextView
    private lateinit var etAddress: EditText
    private lateinit var bSearch: Button
    private lateinit var fakeList: ArrayList<Cuisine>
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_home_tab, null)
        rvCuisines = fragmentView.findViewById(R.id.recycler_view_cuisines_list)
        tvUser = fragmentView.findViewById(R.id.text_view_user)
        etAddress = fragmentView.findViewById(R.id.editText_address)
        bSearch = fragmentView.findViewById(R.id.search_restaurant_listings)

        tvUser.text = "Hi, ${userLoggedIn.fullName}!"

        etAddress.background = null

        etAddress.setText(SharedPreferencesConfig(context!!).getDefaultStreet())

        etAddress.setOnClickListener {
            val intentAutocompleteStreet = Intent(context, AutocompleteStreetActivity::class.java)
            startActivity(intentAutocompleteStreet)
        }

        bSearch.setOnClickListener {
            val address = etAddress.text.toString()
            if (address.isEmpty()) {
                etAddress.error = getString(R.string.text_field_empty)
                etAddress.requestFocus()
            } else {
                val intentGetBusinessListings = Intent(context, ShowRestaurantListingActivity::class.java)
                val bun = Bundle()
                bun.putString(BundleKeys.ADDRESS_SEARCH, address)
                intentGetBusinessListings.putExtras(bun)
                startActivity(intentGetBusinessListings)
            }
        }

        setupFakeList()
        setupRecyclerView()

        StreetSingleton.setStreetSingletonListener(object : StreetSingleton.StreetSingletonListener{
            override fun onStreetSelectedListener(streetSelected: String) {
                etAddress.setText(streetSelected)
            }

        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return fragmentView
    }

    private fun setupFakeList() {
        fakeList = arrayListOf()

        fakeList.add(Cuisine("Bars", context!!.resources.getIdentifier("bars", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Burgers", context!!.resources.getIdentifier("burgers", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Chinese", context!!.resources.getIdentifier("chinese", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Indian", context!!.resources.getIdentifier("indian", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Italian", context!!.resources.getIdentifier("italian", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Japanese", context!!.resources.getIdentifier("japanese", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Mexican", context!!.resources.getIdentifier("mexican", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Pizza", context!!.resources.getIdentifier("pizza", "drawable", context!!.packageName)))
        fakeList.add(Cuisine("Spanish", context!!.resources.getIdentifier("spanish", "drawable", context!!.packageName)))
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.stackFromEnd = false
        rvCuisines.layoutManager = layoutManager

        val adapter = CuisineRecyclerViewAdapter(fakeList)
        adapter.setCategoryRecyclerViewListener(object : CuisineRecyclerViewAdapter.CuisineRecyclerViewListener{
            override fun onClick(position: Int) {
                val category = when (position) {
                    3 -> "indpak"
                    else -> fakeList[position].name.toLowerCase(Locale.ROOT)
                }
                val address = etAddress.text.toString()

                val intentGetBusinessListing = Intent(context, ShowRestaurantListingActivity::class.java)
                intentGetBusinessListing.putExtra(BundleKeys.RESTAURANT_CATEGORY, category)
                intentGetBusinessListing.putExtra(BundleKeys.ADDRESS_SEARCH, address)
                startActivity(intentGetBusinessListing)
            }

        })
        rvCuisines.adapter = adapter
    }

}
