package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
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
                    rvAdapter?.setReservationList(reverseReservationList(updatedUser.reservations))
                }
            }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = ReservationListRecyclerViewAdapter(reverseReservationList(currentUser.reservations))

        binding.recyclerViewReservationsList.layoutManager = rvLayoutManager
        binding.recyclerViewReservationsList.adapter = rvAdapter
    }

    private fun reverseReservationList(userReservationList: ArrayList<Reservation>): ArrayList<Reservation> {
        val reversedReservationList = arrayListOf<Reservation>()

        if (userReservationList.isNotEmpty()) {
            for (i in userReservationList.size-1 downTo 0) {
                reversedReservationList.add(currentUser.reservations[i])
            }
        }

        return reversedReservationList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
