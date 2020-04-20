package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
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
    private val etAddressTitle by lazy { findViewById<TextInputEditText>(R.id.editText_address_title) } // EditText for the Address title
    private val etStreetName by lazy { findViewById<TextInputEditText>(R.id.editText_street_name) } // EditText for the street name
    private val etStreetNumber by lazy { findViewById<TextInputEditText>(R.id.editText_street_number) } // EditText for the street number
    private val etDoorInfo by lazy { findViewById<TextInputEditText>(R.id.editText_door_info) } // EditText for the door info (floor number, door, etc)
    private val etAddressExtras by lazy { findViewById<TextInputEditText>(R.id.editText_address_extras) } // EditText for the Address extras (Apartment, doorbell)
    private val etTown by lazy { findViewById<TextInputEditText>(R.id.editText_town) } // EditText for  the town
    private val etPostalCode by lazy { findViewById<TextInputEditText>(R.id.editText_postal_code) } // EditText for the postal code
    private val bSaveAddress by lazy { findViewById<CardView>(R.id.button_save_address) } // Button to save or modify the address
    private val tvCardView by lazy { findViewById<TextView>(R.id.textview_save_address) } // TextView of the button (which is a CardView)
    private val pbCardView by lazy { findViewById<ProgressBar>(R.id.progressBar_save_address) } // ProgressBar of the button (which is a CardView)
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // Instance of the user currently logged in
    private var isModifyingAddress = false // True if the user user is modifying an existing address, false if otherwise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_address)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) { // If the extras return a non-null Bundle, this means the user is modifying an already existing address
            val addressToModify = userLoggedIn.addresses[intent.extras!!.getInt(BundleKeys.ADDRESS_LIST_POSITION)]

            etAddressTitle.setText(addressToModify.title)
            etStreetName.setText(addressToModify.name)
            etStreetNumber.setText(addressToModify.number)
            etDoorInfo.setText(addressToModify.door)
            etAddressExtras.setText(addressToModify.extras)
            etTown.setText(addressToModify.town)
            etPostalCode.setText(addressToModify.postalCode)

            isModifyingAddress = true
        }

        bSaveAddress.setOnClickListener {
            val addressTitle = etAddressTitle.text.toString()
            val streetName = etStreetName.text.toString()
            val streetNumber = etStreetNumber.text.toString()
            var doorInfo = etDoorInfo.text.toString()
            var addressExtras = etAddressExtras.text.toString()
            val town = etTown.text.toString()
            val postalCode = etPostalCode.text.toString()

            when {
                addressTitle.isEmpty() -> {
                    etAddressTitle.error=getString(R.string.text_field_empty)
                    etAddressTitle.requestFocus()
                }
                streetName.isEmpty() -> {
                    etStreetName.error=getString(R.string.text_field_empty)
                    etStreetName.requestFocus()
                }
                streetNumber.isEmpty() -> {
                    etStreetNumber.error=getString(R.string.text_field_empty)
                    etStreetNumber.requestFocus()
                }
                town.isEmpty() -> {
                    etTown.error=getString(R.string.text_field_empty)
                    etTown.requestFocus()
                }
                postalCode.isEmpty() -> {
                    etPostalCode.error=getString(R.string.text_field_empty)
                    etPostalCode.requestFocus()
                }
                else -> {
                    tvCardView.visibility=View.GONE
                    pbCardView.visibility=View.VISIBLE
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
                pbCardView.visibility=View.GONE
                tvCardView.visibility=View.VISIBLE
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
