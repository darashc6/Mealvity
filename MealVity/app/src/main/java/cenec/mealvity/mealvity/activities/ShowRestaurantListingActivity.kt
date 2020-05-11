package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class ShowRestaurantListingActivity : AppCompatActivity(), SortListByBottomSheet.SortListByBottomSheetListener, FilterListBottomSheet.FilterListBottomSheetListener {
    private val yelpBuilder = CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API)
    private val yelpFusionApi = yelpBuilder.create(YelpFusionApi::class.java)
    private val rvRestaurantList by lazy { findViewById<RecyclerView>(R.id.recycler_view_restaurant_list) }
    private val pbLoading by lazy { findViewById<ProgressBar>(R.id.loading_progress_bar) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private var restaurantList = RestaurantList(arrayListOf())
    private lateinit var address: String
    private lateinit var rvAdapter: RestaurantRecyclerViewAdapter
    private var category: String? = null
    private var sortListOptionSelected = 0
    private val mapCustomParameters by lazy { hashMapOf<String, String>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_restaurant_listing)

        setupToolbar()
        checkBundleExtras()
        getRestaurantListings()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun checkBundleExtras() {
        intent.extras?.let {
            address = it.getString(BundleKeys.ADDRESS_SEARCH)!!
            category = it.getString(BundleKeys.RESTAURANT_CATEGORY)
        }
    }

    private fun getRestaurantListings() {
        if (category == null) {
            getRestaurantListingByAddress()
        } else {
            getRestaurantListingByCategory()
        }
    }

    private fun getRestaurantListingByAddress() {
        val call = yelpFusionApi.getRestaurantListingByAddress(address)

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
                    pbLoading.visibility = View.GONE
                    rvRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.filterList()
                        toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

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
                    pbLoading.visibility = View.GONE
                    rvRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.filterList()
                        toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

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
                    pbLoading.visibility = View.GONE
                    rvRestaurantList.visibility = View.VISIBLE
                    list?.let {
                        restaurantList = it.filterList()
                        toolbar.title = "${restaurantList.results.size} results"
                        onSortList(sortListOptionSelected)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setupRecyclerView() {
        val layoutZoom = LayoutZoom(this, LinearLayoutManager.HORIZONTAL, false)
        layoutZoom.stackFromEnd = false
        rvRestaurantList.layoutManager = layoutZoom

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvRestaurantList)

        rvAdapter = RestaurantRecyclerViewAdapter(restaurantList)
        rvRestaurantList.adapter = rvAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restaurant_listing_options, menu)
        return true
    }

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

    override fun onSortList(newOptionSelected: Int) {
        when (newOptionSelected) {
            0 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByBestMatch())
                rvAdapter.notifyDataSetChanged()
                toolbar.subtitle = resources.getString(R.string.text_sort_list_best_match)
            }
            1 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByRatings())
                rvAdapter.notifyDataSetChanged()
                toolbar.subtitle = resources.getString(R.string.text_sort_list_rating)
            }
            2 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByDistance())
                rvAdapter.notifyDataSetChanged()
                toolbar.subtitle = resources.getString(R.string.text_sort_list_distance)
            }
            3 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByEconomicPrice())
                rvAdapter.notifyDataSetChanged()
                toolbar.subtitle = resources.getString(R.string.text_sort_list_economic_price)
            }
            4 -> {
                rvAdapter.setRestaurantList(restaurantList.filterListByLuxuriousPrice())
                rvAdapter.notifyDataSetChanged()
                toolbar.subtitle = resources.getString(R.string.text_sort_luxurious_price)
            }
        }
        sortListOptionSelected = newOptionSelected
    }

    override fun onApplyFiltersClick(customParameters: HashMap<String, String>) {
        pbLoading.visibility = View.VISIBLE
        rvRestaurantList.visibility = View.GONE
        mapCustomParameters.putAll(customParameters)
        getRestaurantListingByCustomParameters(mapCustomParameters)
    }

    override fun onClearFiltersClick() {
        pbLoading.visibility = View.VISIBLE
        rvRestaurantList.visibility = View.GONE
        mapCustomParameters.clear()
        getRestaurantListings()
    }
}
