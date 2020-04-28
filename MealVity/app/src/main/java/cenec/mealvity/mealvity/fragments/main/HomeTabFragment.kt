package cenec.mealvity.mealvity.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentHomeTabBinding
import cenec.mealvity.mealvity.activities.ShowRestaurantListing
import cenec.mealvity.mealvity.classes.adapters.CuisineRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.restaurant.Categories
import cenec.mealvity.mealvity.classes.restaurant.Cuisine
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import java.util.*
import kotlin.collections.ArrayList

/**
 * Fragment of the Home Tab
 */
class HomeTabFragment : Fragment() {
    private var _binding: FragmentHomeTabBinding? = null
    private val binding get() = _binding
    private lateinit var fakeList: ArrayList<Cuisine>
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeTabBinding.inflate(layoutInflater)

        binding!!.textViewUser.text = "Hi, ${UserSingleton.getInstance().getCurrentUser().fullName}!"

        binding!!.searchRestaurantListings.setOnClickListener {
            val address = binding!!.editTextAddress.text.toString()
            if (address.isEmpty()) {
                binding!!.editTextAddress.error = getString(R.string.text_field_empty)
                binding!!.editTextAddress.requestFocus()
            } else {
                val intentGetBusinessListings = Intent(context, ShowRestaurantListing::class.java)
                val bun = Bundle()
                bun.putString(BundleKeys.ADDRESS_SEARCH, address)
                intentGetBusinessListings.putExtras(bun)
                startActivity(intentGetBusinessListings)
            }
        }

        setupFakeList()
        setupRecyclerView()

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding!!.recyclerViewCategoriesList.layoutManager = layoutManager

        val adapter = CuisineRecyclerViewAdapter(fakeList)
        adapter.setCategoryRecyclerViewListener(object : CuisineRecyclerViewAdapter.CategoryRecyclerViewListener{
            override fun onClick(position: Int) {
                val category = fakeList[position].name.toLowerCase(Locale.ROOT)
                val address = "${userLoggedIn.addresses[0].name}, ${userLoggedIn.addresses[0].number}"

                val intentGetBusinessListing = Intent(context, ShowRestaurantListing::class.java)
                val bun = Bundle()
                bun.putString(BundleKeys.RESTAURANT_CATEGORY, category)
                bun.putString(BundleKeys.ADDRESS_SEARCH, address)
                intentGetBusinessListing.putExtras(bun)
                startActivity(intentGetBusinessListing)
            }

        })
        binding!!.recyclerViewCategoriesList.adapter = adapter
    }

}
