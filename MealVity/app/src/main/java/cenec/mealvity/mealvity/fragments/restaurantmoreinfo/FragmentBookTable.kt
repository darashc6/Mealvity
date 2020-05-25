package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentBookTableBinding
import cenec.mealvity.mealvity.activities.RestaurantMoreInfoActivity
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import cenec.mealvity.mealvity.classes.viewmodels.BookTableViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class FragmentBookTable : Fragment(), OnMapReadyCallback, RestaurantMoreInfoActivity.RestaurantMoreInfoListener {
    private var _binding: FragmentBookTableBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: BookTableViewModel
    private lateinit var cvBookTable: CardView
    private lateinit var restaurantMoreInfo: RestaurantMoreInfo
    private lateinit var googleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookTableBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onInfoLoaded(example: RestaurantMoreInfo) {
        restaurantMoreInfo = example
        binding!!.loadingProgressBar.visibility = View.GONE
        binding!!.layoutFragmentMap.visibility = View.VISIBLE

        setupViewModel()
        setupMap()
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val locationRestaurant = LatLng(restaurantMoreInfo.coordinates.latitude.toDouble(), restaurantMoreInfo.coordinates.longitude.toDouble())
        setupMapStyle()
        setupMapCamera(locationRestaurant)
        setupRestaurantMarker(locationRestaurant)
        googleMap.uiSettings.isMapToolbarEnabled = false
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(BookTableViewModel::class.java)
    }

    private fun setupMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupViews() {
        cvBookTable = binding!!.root.findViewById(R.id.cardview_restaurant_book_table)
        cvBookTable.findViewById<TextView>(R.id.text_view_restaurant_name).text = restaurantMoreInfo.name
        val cvDatePicker = cvBookTable.findViewById<CardView>(R.id.cardView_date_picker)
        val cvHourPicker = cvBookTable.findViewById<CardView>(R.id.cardView_hour_picker)
        val bAddGuest = cvBookTable.findViewById<ImageView>(R.id.button_add_guest)
        val bRemoveGuest = cvBookTable.findViewById<ImageView>(R.id.button_remove_guest)
        val bGoogleMaps = cvBookTable.findViewById<CardView>(R.id.button_google_maps)
        val bVisitYelp = cvBookTable.findViewById<CardView>(R.id.button_browse_yelp)
        val bBookTable = cvBookTable.findViewById<CardView>(R.id.button_book_table)

        cvBookTable.setOnClickListener {
            // Do nothing
        }

        cvDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        cvHourPicker.setOnClickListener {
            showTimePickerDialog()
        }

        bAddGuest.setOnClickListener {
            viewModel.addGuest()
            updateNumberGuests()
        }

        bRemoveGuest.setOnClickListener {
            viewModel.removeGuest()
            updateNumberGuests()
        }

        bGoogleMaps.setOnClickListener {
            openGoogleMapsApp()
        }

        bVisitYelp.setOnClickListener {
            openYelpWebsite()
        }

        bBookTable.setOnClickListener {
            verifiyReservation()
        }
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
                    context,
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

    private fun setupMapCamera(locationRestaurant: LatLng) {
        googleMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(LatLng(locationRestaurant.latitude-0.00015, locationRestaurant.longitude), 17.5f))

    }

    private fun setupRestaurantMarker(locationRestaurant: LatLng) {
        googleMap.addMarker(
            MarkerOptions()
                .position(locationRestaurant))
    }

    private fun verifyDate(dateToVerify: String): Boolean {
        val currentDate = Calendar.getInstance().time
        val dateSelected = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateToVerify)

        return dateSelected!!.after(currentDate)
    }

    private fun updateNumberGuests() {
        val tvNumberGuest = cvBookTable.findViewById<TextView>(R.id.text_view_number_guest)
        if (viewModel.nGuest == 1) {
            tvNumberGuest.text = "${viewModel.nGuest} guest"
        } else {
            tvNumberGuest.text = "${viewModel.nGuest} guests"
        }
    }

    private fun openGoogleMapsApp() {
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${restaurantMoreInfo.coordinates.latitude},${restaurantMoreInfo.coordinates.longitude}?q=${restaurantMoreInfo.name}, ${restaurantMoreInfo.displayFullAddress()}"))
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context!!.packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun openYelpWebsite(){
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(restaurantMoreInfo.yelp_url))
        try {
            startActivity(intentBrowser)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "No application can handle this request. Please install a browser.",  Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(context!!, R.style.DateTimePickerDialog)
        datePickerDialog.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                val dateSelected = "$dayOfMonth/${month+1}/$year"
                if (verifyDate(dateSelected)) {
                    val tvDateSelected = cvBookTable.findViewById<TextView>(R.id.text_view_date_selected)
                    viewModel.setReservationDate(dateSelected)
                    tvDateSelected.text = viewModel.reservationDate
                } else {
                    Toast.makeText(context, "Please select a valid date", Toast.LENGTH_LONG).show()
                }
            }

        })
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(context!!, R.style.DateTimePickerDialog, object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                val hourSelected = "$hourOfDay:$minute"
                val tvHourSelected = cvBookTable.findViewById<TextView>(R.id.text_view_hour_selected)

                viewModel.setReservationTime(hourSelected)
                tvHourSelected.text = viewModel.reservationTime
            }

        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false)
        timePickerDialog.show()
    }

    private fun verifiyReservation() {
        if (viewModel.verifyReservation()) {
            Toast.makeText(context!!, "Reservation done properly", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context!!, "Please fill all the necessary details", Toast.LENGTH_LONG).show()
        }
    }
}
