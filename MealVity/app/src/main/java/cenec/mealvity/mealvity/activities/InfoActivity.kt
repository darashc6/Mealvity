package cenec.mealvity.mealvity.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityInfoBinding
import cenec.mealvity.mealvity.classes.adapters.AutoImageSliderAdapter
import cenec.mealvity.mealvity.classes.adapters.OpeningHoursRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import com.google.gson.Gson

class InfoActivity : AppCompatActivity() {
    private lateinit var restaurantInfo: RestaurantMoreInfo
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        checkBundleExtras()
        setupToolbar()
        setupViews()
    }

    private fun checkBundleExtras() {
        intent.extras?.getString("object")?.let {
            restaurantInfo = convertStringJsonToObject(it)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Restaurant Info"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViews() {
        binding.imageSlider.setSliderAdapter(AutoImageSliderAdapter(restaurantInfo.photos))
        if (restaurantInfo.photos.size > 1) binding.imageSlider.startAutoCycle()
        binding.textViewRestaurantName.text = restaurantInfo.name
        binding.textViewRestaurantCuisines.text = getString(R.string.text_cuisine_list) + ": " + restaurantInfo.displayCategories()
        binding.textViewRestaurantAddress.text = getString(R.string.text_address) + ": " + restaurantInfo.displayFullAddress()
        binding.textViewRestaurantDisplayPhone.text = getString(R.string.text_display_phone) + ": " + restaurantInfo.display_phone

        if (restaurantInfo.hours.isNullOrEmpty()) {
            binding.recyclerViewOpeningHours.visibility = View.GONE
            binding.textViewRestaurantOpeningHours.text = getString(R.string.text_timings_not_available)
        } else {
            setupRecyclerView()
        }

        binding.buttonBrowseYelp.buttonBrowseYelp.setOnClickListener {
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(restaurantInfo.yelp_url))
            try {
                startActivity(intentBrowser)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "No application can handle this request. Please install a browser.",  Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OpeningHoursRecyclerViewAdapter(restaurantInfo.hours[0].open)

        binding.recyclerViewOpeningHours.layoutManager = rvLayoutManager
        binding.recyclerViewOpeningHours.adapter = rvAdapter
        binding.recyclerViewOpeningHours.suppressLayout(true)
    }

    private fun convertStringJsonToObject(stringJson: String): RestaurantMoreInfo {
        val gson = Gson()
        return gson.fromJson(stringJson, RestaurantMoreInfo::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
