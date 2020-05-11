package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivitySaveAddressBinding
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Address
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity where the user can add a new address or modify an existing one
 */
class SaveAddressActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of the Firestore database
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // Instance of the user currently logged in
    private var isModifyingAddress = false // True if the user user is modifying an existing address, false if otherwise
    private lateinit var binding: ActivitySaveAddressBinding // View binding of the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveAddressBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        checkIntentExtras()
        setupToolbar()
        setupViews()
    }

    /**
     * Checks if the activity is receiving anything from the parent activity's bundle (using Intent Extras)
     */
    private fun checkIntentExtras() {
        if (intent.extras != null) { // If the extras return a non-null Bundle, this means the user is modifying an already existing address
            val addressToModify = userLoggedIn.addresses[intent.extras!!.getInt(BundleKeys.ADDRESS_LIST_POSITION)]

            binding.editTextAddressTitle.setText(addressToModify.title)
            binding.editTextStreetName.setText(addressToModify.name)
            binding.editTextStreetNumber.setText(addressToModify.number)
            binding.editTextDoorInfo.setText(addressToModify.door)
            binding.editTextAddressExtras.setText(addressToModify.extras)
            binding.editTextTown.setText(addressToModify.town)
            binding.editTextPostalCode.setText(addressToModify.postalCode)

            isModifyingAddress = true
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
     * Sets up the views in the activity
     */
    private fun setupViews() {
        binding.buttonSaveAddress.setOnClickListener {
            val addressTitle = binding.editTextAddressTitle.text.toString()
            val streetName = binding.editTextStreetName.text.toString()
            val streetNumber = binding.editTextStreetNumber.text.toString()
            var doorInfo = binding.editTextDoorInfo.text.toString()
            var addressExtras = binding.editTextAddressExtras.text.toString()
            val town = binding.editTextTown.text.toString()
            val postalCode = binding.editTextPostalCode.text.toString()

            when {
                addressTitle.isEmpty() -> {
                    binding.editTextAddressTitle.error=getString(R.string.text_field_empty)
                    binding.editTextAddressTitle.requestFocus()
                }
                streetName.isEmpty() -> {
                    binding.editTextStreetName.error=getString(R.string.text_field_empty)
                    binding.editTextStreetName.requestFocus()
                }
                streetNumber.isEmpty() -> {
                    binding.editTextStreetNumber.error=getString(R.string.text_field_empty)
                    binding.editTextStreetNumber.requestFocus()
                }
                town.isEmpty() -> {
                    binding.editTextTown.error=getString(R.string.text_field_empty)
                    binding.editTextTown.requestFocus()
                }
                postalCode.isEmpty() -> {
                    binding.editTextPostalCode.error=getString(R.string.text_field_empty)
                    binding.editTextPostalCode.requestFocus()
                }
                else -> {
                    binding.textviewSaveAddress.visibility=View.GONE
                    binding.progressBarSaveAddress.visibility=View.VISIBLE
                    if (doorInfo.isEmpty()) {
                        doorInfo = ""
                    }
                    if (addressExtras.isEmpty()) {
                        addressExtras = ""
                    }

                    val newAddress=Address(addressTitle, streetName, streetNumber, doorInfo, addressExtras, town, postalCode)
                    if (!isModifyingAddress) {
                        userLoggedIn.addresses.add(newAddress)
                    } else {
                        val position = intent.extras!!.getInt("position_list")
                        userLoggedIn.addresses.removeAt(position)
                        userLoggedIn.addresses.add(position, newAddress)
                    }
                    updateAddressListToDatabase()
                }
            }
        }
    }

    /**
     * Function where the user's address list is updated in the database
     */
    private fun updateAddressListToDatabase() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS)
            .document(firebaseUser!!.uid)
            .update(Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES, userLoggedIn.addresses)
            .addOnCompleteListener { task ->
                binding.textviewSaveAddress.visibility=View.VISIBLE
                binding.progressBarSaveAddress.visibility=View.GONE
                if (task.isSuccessful) {
                    UserSingleton.getInstance().setCurrentUser(userLoggedIn)
                    Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error saving the address. Please try again later", Toast.LENGTH_SHORT).show()
                    Log.d("errorAddressSave", task.exception!!.message!!, task.exception)
                }
            }
    }
}
