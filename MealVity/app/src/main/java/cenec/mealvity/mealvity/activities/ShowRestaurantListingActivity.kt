package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityShowRestaurantListingBinding
import cenec.mealvity.mealvity.classes.adapters.LayoutZoom
import cenec.mealvity.mealvity.classes.adapters.RestaurantRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.bottomsheet.FilterListBottomSheet
import cenec.mealvity.mealvity.classes.bottomsheet.SortListByBottomSheet
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.interfaceapi.YelpFusionApi
import cenec.mealvity.mealvity.classes.restaurant.Restaurant
import cenec.mealvity.mealvity.classes.restaurant.RestaurantList
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity where given an address and/or a category, it will return a list of restaurants
 * This activity uses 2 listeners
 * SortListByBottomSheetListener -> From SortListByBottomSheet
 * FilterListBottomSheetListener -> From FilterListBottomSheet
 */
class ShowRestaurantListingActivity : AppCompatActivity(), SortListByBottomSheet.SortListByBottomSheetListener, FilterListBottomSheet.FilterListBottomSheetListener {
    private val yelpBuilder = CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API) //Retrofit builder for the Yelp Fusion API
    private val yelpFusionApi = yelpBuilder.create(YelpFusionApi::class.java) // API of Yelp Fusion
    private val mapCustomParameters by lazy { hashMapOf<String, String>() } // HashMap with the custom parameters for an API request
    private lateinit var address: String // Address used for querying
    private lateinit var rvAdapter: RestaurantRecyclerViewAdapter // Adapter for the RecyclerView
    private lateinit var binding: ActivityShowRestaurantListingBinding // View binding of the activity
    private var category: String? = null // Category used for querying
    private var sortListOptionSelected = 0 // Option selected for sorting (0 - Best match, 1 - Ratings, 2 - Distance, 3 - Economic price, 4 - Luxurious price)
    private var restaurantList = RestaurantList(arrayListOf()) // List of restaurants

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowRestaurantListingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupToolbar()
        checkBundleExtras()
        getRestaurantListings()
        setupRecyclerView()
    }

    /**
     * Sets up the activity's toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Checks if the activity is receiving anything from the parent activity's bundle (using Intent Extras)
     */
    private fun checkBundleExtras() {
        intent.extras?.let {
            address = it.getString(BundleKeys.ADDRESS_SEARCH)!!
            category = it.getString(BundleKeys.RESTAURANT_CATEGORY)
        }
    }

    /**
     * Depending on the values returned by the Intent Extras, it will return one list or the other
     * If the Intent Extras only returns the address, it will retrieve all the restaurants with the given address
     * If the Intent Extras algo returns the category, it will retrieve all the restaurants with the given address & category
     */
    private fun getRestaurantListings() {
        if (category == null) {
            getRestaurantListingByAddress()
        } else {
            getRestaurantListingByCategory()
        }
    }

    /**
     * Returns a list of restaurants with the given address
     */
    private fun getRestaurantListingByAddress() {
        val call = yelpFusionApi.getRestaurantListingByAddress(address)

        call.enqueue(object : Callback<RestaurantList> {
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Toast.makeText(this@ShowRestaurantListingActivity, resources.getString(R.string.api_call_onFailure), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RestaurantList>,
                response: Response<RestaurantList>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.recyclerViewRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.getListWithFullInfo()
                        binding.toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, "${resources.getString(R.string.api_call_response_unsuccessful)} ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    /**
     * Returns a list of restaurants with the given address and category
     */
    private fun getRestaurantListingByCategory() {
        val call = yelpFusionApi.getRestaurantListByCategory(address, category!!)

        call.enqueue(object : Callback<RestaurantList> {
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Toast.makeText(this@ShowRestaurantListingActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RestaurantList>,
                response: Response<RestaurantList>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.recyclerViewRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.getListWithFullInfo()
                        binding.toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    /**
     * Returns a list of restaurants using the custom parameters using the filter option
     */
    private fun getRestaurantListingByCustomParameters(customParametersMap: HashMap<String, String>) {
        val call = yelpFusionApi.getRestaurantListByCustomParameters(address, customParametersMap)

        call.enqueue(object : Callback<RestaurantList> {
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Toast.makeText(this@ShowRestaurantListingActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RestaurantList>,
                response: Response<RestaurantList>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()
                    binding.loadingProgressBar.visibility = View.GONE
                    binding.recyclerViewRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.getListWithFullInfo()
                        binding.toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    /**
     * Sets up the RecyclerView containing the list of restaurants
     */
    private fun setupRecyclerView() {
        val layoutZoom = LayoutZoom(this, LinearLayoutManager.HORIZONTAL, false)
        layoutZoom.stackFromEnd = false
        binding.recyclerViewRestaurantList.layoutManager = layoutZoom

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewRestaurantList)

        rvAdapter = RestaurantRecyclerViewAdapter(restaurantList)
        rvAdapter.setRestaurantRecyclerViewListener(object : RestaurantRecyclerViewAdapter.RestaurantRecyclerViewListener{
            override fun onMoreInfoClick(restaurantId: String) {
                val intentRestaurantMoreInfo = Intent(this@ShowRestaurantListingActivity, RestaurantMoreInfoActivity::class.java)
                val bun = Bundle()
                bun.putString("restaurantId", restaurantId)
                intentRestaurantMoreInfo.putExtras(bun)
                startActivity(intentRestaurantMoreInfo)
            }

        })
        binding.recyclerViewRestaurantList.adapter = rvAdapter
    }

    /**
     * Overrides the menu options in the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restaurant_listing_options, menu)
        return true
    }

    /**
     * Overrides the functionality of each menu option
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            resources.getString(R.string.text_menu_sort_by) -> {
                val sortByBottomSheet = SortListByBottomSheet(this, sortListOptionSelected)
                sortByBottomSheet.show(supportFragmentManager, "")
            }
            resources.getString(R.string.text_menu_filters) -> {
                val filtersBottomSheet = FilterListBottomSheet(this, mapCustomParameters)
                filtersBottomSheet.show(supportFragmentManager, "")
            }
        }

        return true
    }

    /**
     * Sorts the lists of restaurants given the option selected
     * @param newOptionSelected Option Selected
     * 0 - Best match, 1 - Ratings, 2 - Distance, 3 - Economic price, 4 - Luxurious price
     */
    override fun onSortList(newOptionSelected: Int) {
        when (newOptionSelected) {
            0 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByBestMatch())
                rvAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = resources.getString(R.string.text_sort_list_best_match)
            }
            1 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByRatings())
                rvAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = resources.getString(R.string.text_sort_list_rating)
            }
            2 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByDistance())
                rvAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = resources.getString(R.string.text_sort_list_distance)
            }
            3 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByEconomicPrice())
                rvAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = resources.getString(R.string.text_sort_list_economic_price)
            }
            4 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByLuxuriousPrice())
                rvAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = resources.getString(R.string.text_sort_luxurious_price)
            }
        }
        sortListOptionSelected = newOptionSelected
    }

    /**
     * Applies the filters in the list
     * @param customParameters map of applied filters
     */
    override fun onApplyFiltersClick(customParameters: HashMap<String, String>) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.recyclerViewRestaurantList.visibility = View.GONE
        mapCustomParameters.putAll(customParameters)
        getRestaurantListingByCustomParameters(mapCustomParameters)
    }

    /**
     * Clears all the filters previously applied
     */
    override fun onClearFiltersClick() {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.recyclerViewRestaurantList.visibility = View.GONE
        mapCustomParameters.clear()
        getRestaurantListings()
    }
}
