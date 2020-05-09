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

class ShowRestaurantListingActivity : AppCompatActivity(), SortListByBottomSheet.SortListByBottomSheetListener {
    private val yelpBuilder = CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API)
    private val yelpFusionApi = yelpBuilder.create(YelpFusionApi::class.java)
    private val rvRestaurantList by lazy { findViewById<RecyclerView>(R.id.recycler_view_restaurant_list) }
    private val pbLoading by lazy { findViewById<ProgressBar>(R.id.loading_progress_bar) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private lateinit var address: String
    private lateinit var rl: RestaurantList
    private lateinit var rvAdapter: RestaurantRecyclerViewAdapter
    private var category: String? = null
    private var sortListOptionSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_restaurant_listing)

        setupToolbar()
        checkBundleExtras()
        getRestaurantListings()
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
                    list?.let {
                        rl = it
                        rl.listRestaurant = it.filterList()
                        setupRecyclerView(rl.listRestaurant)
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
                    list?.let {
                        it.listRestaurant = it.filterList()
                        setupRecyclerView(it.listRestaurant)
                    }
                } else {
                    Toast.makeText(this@ShowRestaurantListingActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun setupRecyclerView(list: ArrayList<Restaurant>) {
        pbLoading.visibility = View.GONE
        rvRestaurantList.visibility = View.VISIBLE
        toolbar.title = "${list.size} results"
        toolbar.subtitle = resources.getString(R.string.text_sort_list_best_match)
        val layoutZoom = LayoutZoom(this, LinearLayoutManager.HORIZONTAL, false)
        layoutZoom.stackFromEnd = false
        rvRestaurantList.layoutManager = layoutZoom

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvRestaurantList)

        rvAdapter = RestaurantRecyclerViewAdapter(list)
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
                // Show bottom sheet
            }
        }

        return true
    }

    override fun onSortListClick(newOptionSelected: Int) {
        if (sortListOptionSelected != newOptionSelected) {
            when (newOptionSelected) {
                0 -> {
                    rvAdapter.setRestaurantList(rl.filterListByBestMatch())
                    rvAdapter.notifyDataSetChanged()
                    toolbar.subtitle = resources.getString(R.string.text_sort_list_best_match)
                }
                1 -> {
                    rvAdapter.setRestaurantList(rl.filterListByRatings())
                    rvAdapter.notifyDataSetChanged()
                    toolbar.subtitle = resources.getString(R.string.text_sort_list_rating)
                }
                2 -> {
                    rvAdapter.setRestaurantList(rl.filterListByDistance())
                    rvAdapter.notifyDataSetChanged()
                    toolbar.subtitle = resources.getString(R.string.text_sort_list_distance)
                }
                3 -> {
                    rvAdapter.setRestaurantList(rl.filterListByEconomicPrice())
                    rvAdapter.notifyDataSetChanged()
                    toolbar.subtitle = resources.getString(R.string.text_sort_list_economic_price)
                }
                4 -> {
                    rvAdapter.setRestaurantList(rl.filterListByLuxuriousPrice())
                    rvAdapter.notifyDataSetChanged()
                    toolbar.subtitle = resources.getString(R.string.text_sort_luxurious_price)
                }
            }
            sortListOptionSelected = newOptionSelected
        }
    }
}
