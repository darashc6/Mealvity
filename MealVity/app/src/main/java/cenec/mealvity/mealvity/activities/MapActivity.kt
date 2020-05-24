package cenec.mealvity.mealvity.activities


import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var restaurantMoreInfo: RestaurantMoreInfo
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        checkBundleExtras()
        setupToolbar()
        setupViews()
        setupMap()
    }

    private fun checkBundleExtras() {
        intent.extras!!.getString("object")?.let {
            restaurantMoreInfo = Gson().fromJson(it, RestaurantMoreInfo::class.java)
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.text_title_toolbar_map)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViews() {
        val cvInfo = findViewById<CardView>(R.id.cardview_restaurant_more_info)

        cvInfo.setOnClickListener {
            // Do nothing
        }

        val tvRestaurantName = cvInfo.findViewById<TextView>(R.id.text_view_restaurant_name)
        val tvRestaurantAddress = cvInfo.findViewById<TextView>(R.id.text_view_restaurant_address)
        val tvRestaurantPhone = cvInfo.findViewById<TextView>(R.id.text_view_restaurant_display_phone)
        val bGoogleMaps = cvInfo.findViewById<CardView>(R.id.button_google_maps)
        val bVisitYelp = cvInfo.findViewById<CardView>(R.id.button_browse_yelp)

        tvRestaurantName.text = restaurantMoreInfo.name
        tvRestaurantAddress.text = restaurantMoreInfo.displayFullAddress()
        tvRestaurantPhone.text = restaurantMoreInfo.display_phone

        bGoogleMaps.setOnClickListener {
            // val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${restaurantMoreInfo.coordinates.latitude},${restaurantMoreInfo.coordinates.longitude}?q=${restaurantMoreInfo.name}, ${restaurantMoreInfo.displayFullAddress()}"))
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        }

        bVisitYelp.setOnClickListener {
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(restaurantMoreInfo.yelp_url))
            try {
                startActivity(intentBrowser)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "No application can handle this request. Please install a browser.",  Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        }
    }

    private fun setupMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.layout_fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val locationRestaurant = LatLng(restaurantMoreInfo.coordinates.latitude.toDouble(), restaurantMoreInfo.coordinates.longitude.toDouble())

        setupMapStyle()
        setupMapCamera(locationRestaurant)
        setupRestaurantMarker(locationRestaurant)
        googleMap.uiSettings.isMapToolbarEnabled = false
    }

    private fun setupMapCamera(locationRestaurant: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory
            .newLatLngZoom(locationRestaurant, 17.5f))
    }

    private fun setupRestaurantMarker(locationRestaurant: LatLng) {
        googleMap.addMarker(MarkerOptions()
            .position(locationRestaurant))
    }

    /**
     * Sets a map style for the Google Maps
     * You can create custom map styles with: https://mapstyle.withgoogle.com/
     * Once you finish styling in the website, copy-paste into a json file
     */
    private fun setupMapStyle() {
        // Customize the styling of the base map using a JSON object defined
        // in a raw resource file.
        try {
            val isSuccess = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style
                )
            )

            if (!isSuccess) {
                Log.e("debugMap", "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("debugMap", "Can't find style. Error: ", e)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
