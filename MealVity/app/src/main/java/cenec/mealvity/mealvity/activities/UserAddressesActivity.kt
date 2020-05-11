package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.classes.viewmodels.UserViewModel
import cenec.mealvity.mealvity.fragments.profileaddress.AddressListFragment
import cenec.mealvity.mealvity.fragments.profileaddress.EmptyAddressListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Activity containing the fragments related to the user's address list
 */
class UserAddressesActivity : AppCompatActivity() {
    private val fabAddAddress by lazy { findViewById<FloatingActionButton>(R.id.fab_add_address) } // Button to add a new address
    private val fragmentWithEmptyList by lazy { EmptyAddressListFragment() } // Instance of fragment with no address in list
    private val fragmentWithList by lazy { AddressListFragment() } // Instance of fragment containing the user's address list
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // Instance of the user currently logged in
    private lateinit var userViewModel: UserViewModel // ViewModel of the User
    private var showingAddressList = false // True if showing the fragment containing the user's address list, false if otherwise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_addresses)

        setupToolbar()
        setupViewModel()
        setupInitialFragment()
        setupViews()
    }

    /**
     * Sets up the toolbar of the activity
     */
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the ViewModel for the activity
     * A ViewModel is used to update user's data in the UI
     */
    private fun setupViewModel() {
        userViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.setUserLiveData(userLoggedIn)
    }

    /**
     * Sets up the initial fragment of the activity
     * If the user doesn't have any addresses in his list, it will show a fragment with no list
     */
    private fun setupInitialFragment() {
        if (userLoggedIn.addresses.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .add(R.id.layout_fragment, fragmentWithEmptyList)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.layout_fragment, fragmentWithList)
                .commit()
        }
    }

    /**
     * Sets up the views in the activity
     */
    private fun setupViews() {
        fabAddAddress.setOnClickListener {
            val intentAddAddress=Intent(this, SaveAddressActivity::class.java)
            startActivity(intentAddAddress)
        }

        // Each time the user's data is updated, we send that data to the ViewModel, so that we can observe the changes in the UI
        UserSingleton.getInstance().setUserModelListener(object : UserSingleton.UserSingletonListener {
            override fun onUserUpdate(updatedUser: User) {
                userViewModel.setUserLiveData(updatedUser)
                if (updatedUser.addresses.isNotEmpty()) {
                    if (!showingAddressList) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.layout_fragment, fragmentWithList)
                            .commitAllowingStateLoss()
                        showingAddressList = true
                    }
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.layout_fragment, fragmentWithEmptyList)
                        .commitAllowingStateLoss()
                    showingAddressList = false
                }
            }

        })
    }

    /**
     * Return's the User ViewModel
     * This is used in AddressListFragment
     */
    fun getUserViewModel(): UserViewModel {
        return userViewModel
    }

    /**
     * Overrides the home button displayed in the toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
