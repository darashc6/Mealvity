package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityRestaurantMoreInfoBinding
import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter2
import cenec.mealvity.mealvity.classes.interfaceapi.YelpFusionApi
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import cenec.mealvity.mealvity.classes.retrofit.CustomRetrofitBuilder
import cenec.mealvity.mealvity.fragments.restaurantmoreinfo.FragmentBookTable
import cenec.mealvity.mealvity.fragments.restaurantmoreinfo.FragmentOrder
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantMoreInfoActivity : AppCompatActivity() {
    private val fragmentBookTable by lazy { FragmentBookTable() }
    private val fragmentOrder by lazy { FragmentOrder() }
    private val yelpBuilder by lazy { CustomRetrofitBuilder.createRetrofitBuilder(ApiAccess.URL_YELP_FUSION_API) }
    private val yelpFusionApi by lazy { yelpBuilder.create(YelpFusionApi::class.java) }
    private var restaurantMoreInfo: RestaurantMoreInfo? = null
    private lateinit var restaurantId: String
    private lateinit var aListener: RestaurantMoreInfoListener
    private lateinit var binding: ActivityRestaurantMoreInfoBinding


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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager() {
        val fragmentAdapter = FragmentAdapter2(this, listOf(fragmentBookTable, fragmentOrder))

        binding.viewPagerFragment.adapter = fragmentAdapter
        binding.viewPagerFragment.isUserInputEnabled = false

    }

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

    private fun checkBundleExtras() {
        intent.extras?.let {
            restaurantId = it.getString("restaurantId", "")
        }
    }

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

    fun getRestaurantMoreInfo(): RestaurantMoreInfo? {
        return restaurantMoreInfo
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_restaurant_more_info_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (restaurantMoreInfo == null) {
            Toast.makeText(this, "Please wait while we load all the info", Toast.LENGTH_SHORT).show()
        } else {
            when (item.title) {
                getString(R.string.title_more_info_info) -> {
                    val intentMoreInfo = Intent(this, InfoActivity::class.java)
                    intentMoreInfo.putExtra("object", convertObjectToStringJson(restaurantMoreInfo!!))

                    startActivity(intentMoreInfo)
                }
                getString(R.string.title_more_info_map) -> {
                    val intentMap = Intent(this, MapActivity::class.java)
                    intentMap.putExtra("object", convertObjectToStringJson(restaurantMoreInfo!!))

                    startActivity(intentMap)
                }
            }
        }
        return true
    }

    private fun convertObjectToStringJson(restaurantMoreInfo: RestaurantMoreInfo): String {
        val gson = Gson()
        return gson.toJson(restaurantMoreInfo, RestaurantMoreInfo::class.java)
    }

    interface RestaurantMoreInfoListener {
        fun onInfoLoaded(example: RestaurantMoreInfo)
    }
}
