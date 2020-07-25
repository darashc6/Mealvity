package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityReservationsBinding
import cenec.mealvity.mealvity.classes.adapters.ReservationListRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.reservations.Reservation
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Order
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

/**
 * Activity showing all the reservations the logged in user has made
 */
class ReservationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationsBinding // View binding of the activity
    private var rvAdapter: ReservationListRecyclerViewAdapter? = null // Adapter for the RecyclerView containing a list of reservations
    private val currentUser by lazy { UserSingleton.getInstance().getCurrentUser()} // User currently logged in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        listenForDatabaseChanges()
    }

    /**
     * Listens for any changes made in the database
     * If changes are made in the database, it will update the current screen
     */
    private fun listenForDatabaseChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserId = UserSingleton.getInstance().getCurrentUser().userId

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUserId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot!!.exists()) {
                    val updatedUser = documentSnapshot.toObject(User::class.java)
                    UserSingleton.getInstance().setCurrentUser(updatedUser!!)
                    checkUserReservationList()
                }
            }
    }

    /**
     * Sets up the activity's toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the RecyclerView containing a list of reservations
     */
    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = ReservationListRecyclerViewAdapter(currentUser.showAllReservations())

        binding.recyclerViewReservationsList.layoutManager = rvLayoutManager
        binding.recyclerViewReservationsList.adapter = rvAdapter
    }

    /**
     * Checks if the user has a list of reservations made
     * Layouts will be different depending on the list (If it's empty or not)
     */
    private fun checkUserReservationList() {
        if (currentUser.reservations.isEmpty()) {
            binding.textViewEmptyReservationList.visibility = View.VISIBLE
            binding.recyclerViewReservationsList.visibility = View.GONE
        } else {
            if (binding.recyclerViewReservationsList.visibility == View.GONE) {
                binding.recyclerViewReservationsList.visibility = View.VISIBLE
                binding.textViewEmptyReservationList.visibility = View.GONE
            }
            rvAdapter?.setReservationList(currentUser.showAllReservations())
        }
    }

    /**
     * Overrides the behaviour of the buttons in the options menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_show_all -> rvAdapter?.setReservationList(currentUser.showAllReservations())
            R.id.menu_show_pending -> rvAdapter?.setReservationList(currentUser.showPendingReservations())
            R.id.menu_show_accepted -> rvAdapter?.setReservationList(currentUser.showAcceptedReservations())
            R.id.menu_show_rejected -> rvAdapter?.setReservationList(currentUser.showRejectedReservations())
        }

        return true
    }

    /**
     * Overrides the options menu in the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter_reservations, menu)
        return true
    }
}
