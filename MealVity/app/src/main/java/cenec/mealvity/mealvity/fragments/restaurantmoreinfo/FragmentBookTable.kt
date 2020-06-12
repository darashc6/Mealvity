package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentBookTableBinding
import cenec.mealvity.mealvity.activities.RestaurantMoreInfoActivity
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.reservations.Reservation
import cenec.mealvity.mealvity.classes.user.UserDetails
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Order
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class FragmentBookTable : Fragment(), OnMapReadyCallback, RestaurantMoreInfoActivity.RestaurantMoreInfoListener {
    private var _binding: FragmentBookTableBinding? = null
    private val binding get() = _binding
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }
    private var tableBooked = false
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var newReservation: Reservation
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

        setupMap()
        checkBookedTable()
    }

    private fun checkBookedTable() {
        if (userLoggedIn.reservations.isNotEmpty()) {
            for (reservation in userLoggedIn.reservations) {
                if (reservation.restaurantName == restaurantMoreInfo.name) {
                    tableBooked = true
                    newReservation = reservation
                    break
                }
            }
        }

        if (!tableBooked) {
            binding!!.cardviewRestaurantBookTable.root.visibility = View.VISIBLE
            binding!!.cardviewRestaurantReservationDetails.root.visibility = View.GONE
            setupNewReservation()
            setupNewReservationViews()
        } else {
            binding!!.cardviewRestaurantBookTable.root.visibility = View.GONE
            binding!!.cardviewRestaurantReservationDetails.root.visibility = View.VISIBLE
            setupReservationDetailsViews()
        }
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

    private fun setupNewReservation() {
        val currentUser = UserSingleton.getInstance().getCurrentUser()
        val userReservationDetails =
            UserDetails(
                currentUser.userId,
                currentUser.fullName,
                currentUser.phoneNumber,
                currentUser.email
            )
        newReservation = Reservation(userReservationDetails)
        newReservation.restaurantName = restaurantMoreInfo.name
    }

    private fun setupMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupNewReservationViews() {
        binding!!.cardviewRestaurantBookTable.textViewRestaurantName.text = restaurantMoreInfo.name

        binding!!.cardviewRestaurantBookTable.root.setOnClickListener {
            // Do nothing
        }

        binding!!.cardviewRestaurantBookTable.cardViewDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        binding!!.cardviewRestaurantBookTable.cardViewHourPicker.setOnClickListener {
            showTimePickerDialog()
        }

        binding!!.cardviewRestaurantBookTable.buttonAddGuest.setOnClickListener {
            newReservation.addGuest()
            updateNumberGuests()
        }

        binding!!.cardviewRestaurantBookTable.buttonRemoveGuest.setOnClickListener {
            newReservation.removeGuest()
            updateNumberGuests()
        }

        binding!!.cardviewRestaurantBookTable.buttonGoogleMaps.setOnClickListener {
            openGoogleMapsApp()
        }

        binding!!.cardviewRestaurantBookTable.buttonBrowseYelp.setOnClickListener {
            openYelpWebsite()
        }

        binding!!.cardviewRestaurantBookTable.buttonBookTable.setOnClickListener {
            verifiyReservation()
        }
    }

    private fun setupReservationDetailsViews() {
        binding!!.cardviewRestaurantReservationDetails.textViewRestaurantName.text = restaurantMoreInfo.name
        binding!!.cardviewRestaurantReservationDetails.textViewReferenceNumber.text = "Reference Nº: ${newReservation.referenceNumber}"
        binding!!.cardviewRestaurantReservationDetails.textViewDate.text = "Date: ${newReservation.date}"
        binding!!.cardviewRestaurantReservationDetails.textViewHour.text = "Time: ${newReservation.time}"
        binding!!.cardviewRestaurantReservationDetails.textViewNumberGuest.text = "Nº Guests: ${newReservation.nGuests}"
        binding!!.cardviewRestaurantReservationDetails.textViewReservationStatus.text = "Status: ${newReservation.reservationStatus}"

        binding!!.cardviewRestaurantReservationDetails.root.setOnClickListener {

        }

        binding!!.cardviewRestaurantReservationDetails.buttonGoogleMaps.setOnClickListener {
            openGoogleMapsApp()
        }

        binding!!.cardviewRestaurantReservationDetails.buttonBrowseYelp.setOnClickListener {
            openYelpWebsite()
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
        if (newReservation.nGuests == 1) {
            binding!!.cardviewRestaurantBookTable.textViewNumberGuest.text = "${newReservation.nGuests} guest"
        } else {
            binding!!.cardviewRestaurantBookTable.textViewNumberGuest.text = "${newReservation.nGuests} guests"
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
                    newReservation.date = dateSelected
                    binding!!.cardviewRestaurantBookTable.textViewDateSelected.text = newReservation.date
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
                val date = Calendar.getInstance()
                date.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), hourOfDay, minute)

                newReservation.time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date.time)
                binding!!.cardviewRestaurantBookTable.textViewHourSelected.text = newReservation.time
            }

        }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), false)
        timePickerDialog.show()
    }

    private fun verifiyReservation() {
        if (newReservation.date.isNotEmpty() && newReservation.time.isNotEmpty()) {
            userLoggedIn.addReservation(newReservation)
            addReservationToRestaurantDatabase()
        } else {
            Toast.makeText(context!!, "Please fill all the necessary details", Toast.LENGTH_LONG).show()
        }
    }

    private fun addReservationToRestaurantDatabase() {
        val restaurantNameString = restaurantMoreInfo.name.replace(" ", "").toLowerCase(Locale.ROOT)

        mFirebaseFirestore.collection("restaurants").document(restaurantNameString)
            .update("reservations", FieldValue.arrayUnion(newReservation))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInDatabase()
                } else {
                    createRestaurantDatabase(restaurantNameString)
                    println(task.exception)
                }
            }
    }

    private fun createRestaurantDatabase(restaurantName: String) {
        mFirebaseFirestore.collection("restaurants").document(restaurantName)
            .set(hashMapOf(
                "name" to restaurantName,
                "reservations" to arrayListOf<Reservation>(),
                "orders" to arrayListOf<Order>()
            ))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addReservationToRestaurantDatabase()
                } else {
                    Toast.makeText(context, "Error creating database", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun updateUserInDatabase() {
        val currentUser = UserSingleton.getInstance().getCurrentUser()

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUser.userId!!)
            .set(currentUser).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Task successfull", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Task failed", Toast.LENGTH_LONG).show()
                }
            }
    }
}
