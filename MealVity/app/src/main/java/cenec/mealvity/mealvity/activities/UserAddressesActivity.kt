package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityUserAddressesBinding
import cenec.mealvity.mealvity.classes.adapters.AddressRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.sulek.ssml.SSMLLinearLayoutManager

/**
 * Activity containing the fragments related to the user's address list
 */
class UserAddressesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserAddressesBinding // View binding for the activity
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // Instance of the user currently logged in
    private var rvAdapter: AddressRecyclerViewAdapter? = null // Adapter for the RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupUserListener()
        checkAddressList()
    }

    /**
     * Sets up the toolbar of the activity
     */
    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the RecyclerView, containing a list of Addresses
     */
    private fun setupRecyclerView() {
        binding.recyclerViewAddressList.layoutManager= SSMLLinearLayoutManager(this)
        rvAdapter=AddressRecyclerViewAdapter(UserSingleton.getInstance().getCurrentUser().addresses)
        rvAdapter?.setOnAddressRecyclerViewListener(object : AddressRecyclerViewAdapter.AddressRecyclerViewListener{
            override fun onAddressDelete(position: Int) {
                deleteAddressFromDatabase(position)
            }

            override fun onAddressEdit(position: Int) {
                val intentEditAddress = Intent(this@UserAddressesActivity, SaveAddressActivity::class.java)
                val bun = Bundle()
                bun.putInt(BundleKeys.ADDRESS_LIST_POSITION, position)
                intentEditAddress.putExtras(bun)
                startActivity(intentEditAddress)
            }

        })
        binding.recyclerViewAddressList.adapter=rvAdapter
    }

    /**
     * Sets up the views in the activity
     */
    private fun checkAddressList() {
        if (userLoggedIn.addresses.isNotEmpty()) {
            setupRecyclerView()
            binding.recyclerViewAddressList.visibility = View.VISIBLE
            binding.textViewEmptyAddressList.visibility = View.GONE
        } else {
            binding.recyclerViewAddressList.visibility = View.GONE
            binding.textViewEmptyAddressList.visibility = View.VISIBLE
        }
    }

    /**
     * Sets up the listener to the User's Singleton class
     */
    private fun setupUserListener() {
        UserSingleton.getInstance().setUserModelListener(object : UserSingleton.UserSingletonListener {
            override fun onUserUpdate(updatedUser: User) {
                rvAdapter?.setAddressList(updatedUser.addresses)
                checkAddressList()
            }

        })
    }

    /**
     * Deletes an address from the user's list given the list's position
     * @param position Index of the list
     */
    private fun deleteAddressFromDatabase(position: Int) {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userLoggedIn = UserSingleton.getInstance().getCurrentUser()
        userLoggedIn.addresses.removeAt(position)

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS)
            .document(firebaseUser!!.uid)
            .update(Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES, userLoggedIn.addresses)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UserSingleton.getInstance().setCurrentUser(userLoggedIn)
                    rvAdapter?.setAddressList(userLoggedIn.addresses)
                    checkAddressList()
                    Toast.makeText(this, "Address deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error deleting address, please try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Overrides the menu displayed in the toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_addresses_toolbar, menu)
        return true
    }

    /**
     * Overrides the buttons displayed in the toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.add_address -> {
                val intentAddAddress=Intent(this, SaveAddressActivity::class.java)
                startActivity(intentAddAddress)
            }
        }

        return true
    }
}
