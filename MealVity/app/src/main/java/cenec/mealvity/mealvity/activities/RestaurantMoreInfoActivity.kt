package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityRestaurantMoreInfoBinding
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter2
import cenec.mealvity.mealvity.classes.interfaceapi.YelpFusionApi
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.fragments.restaurantmoreinfo.FragmentBookTable
import cenec.mealvity.mealvity.fragments.restaurantmoreinfo.FragmentOrder
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Activity containing FragmentBookTable and FragmentOrder
 */
class RestaurantMoreInfoActivity : AppCompatActivity() {
    private val fragmentBookTable by lazy { FragmentBookTable() } // Instance of FragmentBookTable
    private val fragmentOrder by lazy { FragmentOrder() } // Instance of FragmentOrder
    private val yelpBuilder by lazy { CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API) } // Retrofit builder for the Yelp Fusion APO
    private val yelpFusionApi by lazy { yelpBuilder.create(YelpFusionApi::class.java) } // Instance of the API
    private var restaurantMoreInfo: RestaurantMoreInfo? = null // // Object containing extra info of the restaurant
    private lateinit var restaurantId: String // Yelp id of the restaurant
    private lateinit var aListener: RestaurantMoreInfoListener // Listener of the activity
    private lateinit var binding: ActivityRestaurantMoreInfoBinding // View binding of the activity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantMoreInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupToolbar()
        setupViewPager()
        setupNavView()
        checkBundleExtras()
        getRestaurantInfo()
    }

    /**
     * Sets up the activity's toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the viewpager for navigation between fragments
     */
    private fun setupViewPager() {
        val fragmentAdapter = FragmentAdapter2(this, listOf(fragmentBookTable, fragmentOrder))

        binding.viewPagerFragment.adapter = fragmentAdapter
        binding.viewPagerFragment.isUserInputEnabled = false

    }

    /**
     * Sets up the bottom navigation view
     */
    private fun setupNavView() {
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.title) {
                getString(R.string.title_more_info_book_table) -> {
                    binding.viewPagerFragment.setCurrentItem(0, true)
                }
                getString(R.string.title_more_info_order) -> {
                    binding.viewPagerFragment.setCurrentItem(1, true)
                }
            }
            true
        }
    }

    /**
     * Receives the yelp id of the restaurant
     */
    private fun checkBundleExtras() {
        intent.extras?.let {
            restaurantId = it.getString("restaurantId", "")
        }
    }

    /**
     * Returns all the info of the restaurant from the API, given the restaurant's yelp id
     */
    private fun getRestaurantInfo() {
        val call = yelpFusionApi.getRestaurantInfoById(restaurantId)

        call.enqueue(object : Callback<RestaurantMoreInfo> {
            override fun onFailure(call: Call<RestaurantMoreInfo>, t: Throwable) {
                println(t)
                Toast.makeText(this@RestaurantMoreInfoActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RestaurantMoreInfo>,
                response: Response<RestaurantMoreInfo>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        restaurantMoreInfo = it
                        aListener = fragmentBookTable
                        binding.toolbar.title = restaurantMoreInfo!!.name
                        aListener.onInfoLoaded(restaurantMoreInfo!!)
                    }
                } else {
                    Toast.makeText(this@RestaurantMoreInfoActivity, response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    /**
     * Overrides the options menu in the activity's toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restaurant_more_info_toolbar, menu)
        return true
    }

    /**
     * Overrides the behaviour of each menu item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (restaurantMoreInfo == null) {
            Toast.makeText(this, "Please wait while we load all the info", Toast.LENGTH_SHORT).show()
        } else {
            when (item.itemId) {
                R.id.more_info_info -> {
                    val intentMoreInfo = Intent(this, InfoActivity::class.java)
                    intentMoreInfo.putExtra("object", restaurantMoreInfo)
                    startActivity(intentMoreInfo)
                }
                android.R.id.home -> finish()
            }
        }
        return true
    }

    /**
     * Interface of the current activity
     */
    interface RestaurantMoreInfoListener {
        /**
         * Executed when all the info from the API is retrieved
         * @param example Object with all the restaurant info
         */
        fun onInfoLoaded(example: RestaurantMoreInfo)
    }
}
