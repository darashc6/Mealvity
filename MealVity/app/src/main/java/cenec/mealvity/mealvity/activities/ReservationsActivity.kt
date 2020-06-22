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
 * TODO
 */
class ReservationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationsBinding
    private var rvAdapter: ReservationListRecyclerViewAdapter? = null
    private val currentUser by lazy { UserSingleton.getInstance().getCurrentUser()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        listenForDatabaseChanges()
    }

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

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = ReservationListRecyclerViewAdapter(currentUser.showAllReservations())

        binding.recyclerViewReservationsList.layoutManager = rvLayoutManager
        binding.recyclerViewReservationsList.adapter = rvAdapter
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter_reservations, menu)
        return true
    }
}
