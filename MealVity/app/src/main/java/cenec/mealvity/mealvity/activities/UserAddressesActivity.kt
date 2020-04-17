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

class UserAddressesActivity : AppCompatActivity() {
    private val fabAddAddress by lazy { findViewById<FloatingActionButton>(R.id.fab_add_address) }
    private val fragmentWithEmptyList by lazy { EmptyAddressListFragment() }
    private val fragmentWithList by lazy { AddressListFragment() }
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }
    private lateinit var userViewModel: UserViewModel
    private var showingAddressList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_addresses)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        userViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.setUserLiveData(UserSingleton.getInstance().getCurrentUser())

        if (userLoggedIn.addresses.isEmpty()) {
            supportFragmentManager.beginTransaction()
                .add(R.id.layout_fragment, fragmentWithEmptyList)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.layout_fragment, fragmentWithList)
                .commit()
        }

        fabAddAddress.setOnClickListener {
            val intentAddAddress=Intent(this, SaveAddressActivity::class.java)
            startActivity(intentAddAddress)
        }

        UserSingleton.getInstance().setUserModelListener(object : UserSingleton.UserModelListener {
            override fun onUserUpdate(updatedUser: User) {
                userViewModel.setUserLiveData(updatedUser)
                if (userViewModel.getUserLiveData().value!!.addresses.isNotEmpty()) {
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

    fun getUserViewModel(): UserViewModel {
        return userViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
