package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.interfaceapi.YelpFusionApi
import cenec.mealvity.mealvity.classes.restaurant.Restaurant
import cenec.mealvity.mealvity.classes.restaurant.RestaurantList
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowRestaurantListing : AppCompatActivity() {
    private val yelpBuilder = CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API)
    private val yelpFusionApi = yelpBuilder.create(YelpFusionApi::class.java)
    private val rvRestaurantList by lazy { findViewById<RecyclerView>(R.id.recycler_view_restaurant_list) }
    private val pbLoading by lazy { findViewById<ProgressBar>(R.id.loading_progress_bar) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private lateinit var address: String
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_restaurant_listing)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        intent.extras?.let {
            address = it.getString(BundleKeys.ADDRESS_SEARCH)!!
            category = it.getString(BundleKeys.RESTAURANT_CATEGORY)
        }

        if (category == null) {
            getRestaurantListingByAddress()
        } else {
            getRestaurantListingByCategory()
        }
    }

    private fun setupRecyclerView(list: ArrayList<Restaurant>) {
        pbLoading.visibility = View.GONE
        rvRestaurantList.visibility = View.VISIBLE
        toolbar.title = "${list.size} results"
        val layoutZoom = LayoutZoom(this, LinearLayoutManager.HORIZONTAL, false)
        layoutZoom.stackFromEnd = false
        rvRestaurantList.layoutManager = layoutZoom

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvRestaurantList)

        val rvAdapter = RestaurantRecyclerViewAdapter(list)
        rvRestaurantList.adapter = rvAdapter
    }

    private fun getRestaurantListingByAddress() {
        val call = yelpFusionApi.getRestaurantListingByAddress(address)

        call.enqueue(object : Callback<RestaurantList> {
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Toast.makeText(this@ShowRestaurantListing, "onFailure", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@ShowRestaurantListing, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getRestaurantListingByCategory() {
        val call = yelpFusionApi.getRestaurantListByCategory(address, category!!)

        call.enqueue(object : Callback<RestaurantList> {
            override fun onFailure(call: Call<RestaurantList>, t: Throwable) {
                Toast.makeText(this@ShowRestaurantListing, "onFailure", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@ShowRestaurantListing, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}
