package cenec.mealvity.mealvityforowners

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase
import cenec.mealvity.mealvityforowners.core.adapter.ViewPagerAdapter
import cenec.mealvity.mealvityforowners.core.viewmodels.RestaurantDatabaseViewModel
import cenec.mealvity.mealvityforowners.databinding.ActivityFragmentContainerBinding
import cenec.mealvity.mealvityforowners.features.orderlist.OrderListFragment
import cenec.mealvity.mealvityforowners.features.reservationlist.ReservationListFragment
import com.google.android.material.internal.NavigationMenu
import com.google.firebase.firestore.*
import io.github.yavski.fabspeeddial.FabSpeedDial

/**
 * Activity where the fragments are located
 */
class FragmentContainerActivity : AppCompatActivity() {
    private val reservationListFragment by lazy { ReservationListFragment() } // Instance of the fragment with a reservations list
    private val orderListFragment by lazy { OrderListFragment() } // Instance of the fragment with a orders list
    private var dbRestaurant = RestaurantDatabaseSingleton.getInstance().getRestaurantDatabase() // Instance of the restaurant database
    private var aListener: FragmentContainerActivityListener? = null // Activity's listener
    private var filterOptSelected = 0 // Options for filtering (0 - All, 1 - PENDING, 2 - ACCEPTED, 3 - REJECTED)
    private lateinit var binding: ActivityFragmentContainerBinding // View binding for the activity
    private lateinit var viewModel: RestaurantDatabaseViewModel // ViewModel of the RestaurantDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupViewPager()
        listenForDatabaseChanges()
        setupFab()
    }

    /**
     * Sets up the view model
     */
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(RestaurantDatabaseViewModel::class.java)
    }

    /**
     * Sets up the viewpager for the fragments
     */
    private fun setupViewPager() {
        val vpAdapter = ViewPagerAdapter(supportFragmentManager, arrayListOf(reservationListFragment, orderListFragment))

        binding.viewPagerFragment.adapter = vpAdapter
        binding.viewPagerFragment.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.viewPagerFragment.setCurrentItem(position, true)
                if (position == 0) {
                    binding.bottomNav.menu.findItem(R.id.bottom_nav_reservations).isChecked = true
                } else if (position == 1) {
                    binding.bottomNav.menu.findItem(R.id.bottom_nav_orders).isChecked = true
                }
            }
        })

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_reservations -> {
                    binding.viewPagerFragment.setCurrentItem(0, true)
                }
                R.id.bottom_nav_orders -> {
                    binding.viewPagerFragment.setCurrentItem(1, true)
                }
            }
            true
        }
    }

    /**
     * Sets up the floating action button, where you can filter the lists
     */
    private fun setupFab() {
        binding.fabFilterList.setMenuListener(object : FabSpeedDial.MenuListener{
            override fun onPrepareMenu(p0: NavigationMenu?): Boolean {
                return true
            }

            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.filter_show_pending -> filterOptSelected = 1
                    R.id.filter_show_accepted -> filterOptSelected = 2
                    R.id.filter_show_rejected -> filterOptSelected = 3
                    R.id.filter_show_all -> filterOptSelected = 0
                }
                if (binding.bottomNav.menu.findItem(R.id.bottom_nav_reservations).isChecked) {
                    aListener = reservationListFragment
                } else if (binding.bottomNav.menu.findItem(R.id.bottom_nav_orders).isChecked) {
                    aListener = orderListFragment
                }
                aListener!!.onFilterOptionSelected(filterOptSelected)
                return true
            }

            override fun onMenuClosed() {
                // Do nothing
            }

        })
    }

    /**
     * Listens for any changes made in the database
     */
    private fun listenForDatabaseChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("restaurants")
            .document(dbRestaurant.name)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot!!.exists()) {
                    dbRestaurant = documentSnapshot.toObject(RestaurantDatabase::class.java)!!
                    viewModel.setRestaurantDatabase(dbRestaurant)
                }
            }
    }

    /**
     * Returns the RestaurantDatabase ViewModel
     * @return RestaurantDatabase ViewModel
     */
    fun getViewModel(): RestaurantDatabaseViewModel {
        return viewModel
    }

    /**
     * Interface for the activity
     */
    interface FragmentContainerActivityListener{
        /**
         * Triggered whenever a filter option is selected
         * @param filterOptSelected Filter option selected
         */
        fun onFilterOptionSelected(filterOptSelected: Int)
    }
}
