package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityReservationsBinding
import cenec.mealvity.mealvity.classes.adapters.ReservationListRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.config.DatabaseConfig
import cenec.mealvity.mealvity.classes.reservations.Reservation
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import java.util.*
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
        setupDatabaseConfig()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupDatabaseConfig() {
        DatabaseConfig.setDatabaseConfigListener(object : DatabaseConfig.DatabaseConfigListener{
            override fun onTaskSuccessful() {
                // Do nothing
            }

            override fun onTaskFailed() {
                // Do nothing
            }

            override fun onUserUpdated(userUpdated: User) {
                UserSingleton.getInstance().setCurrentUser(userUpdated)
                rvAdapter?.setReservationList(userUpdated.reservations.reversed() as ArrayList<Reservation>)
                println("HOLAAAAA")
            }

        })
        DatabaseConfig.listenForUserChanges()
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvAdapter = ReservationListRecyclerViewAdapter(currentUser.reservations.reversed() as ArrayList<Reservation>)

        binding.recyclerViewReservationsList.layoutManager = rvLayoutManager
        binding.recyclerViewReservationsList.adapter = rvAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
