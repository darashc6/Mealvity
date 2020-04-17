package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Address
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SaveAddressActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val etAddressTitle by lazy { findViewById<TextInputEditText>(R.id.editText_address_title) }
    private val etStreetName by lazy { findViewById<TextInputEditText>(R.id.editText_street_name) }
    private val etStreetNumber by lazy { findViewById<TextInputEditText>(R.id.editText_street_number) }
    private val etDoorInfo by lazy { findViewById<TextInputEditText>(R.id.editText_door_info) }
    private val etAddressExtras by lazy { findViewById<TextInputEditText>(R.id.editText_address_extras) }
    private val etTown by lazy { findViewById<TextInputEditText>(R.id.editText_town) }
    private val etPostalCode by lazy { findViewById<TextInputEditText>(R.id.editText_postal_code) }
    private val bSaveAddress by lazy { findViewById<CardView>(R.id.button_save_address) }
    private val tvCardView by lazy { findViewById<TextView>(R.id.textview_save_address) }
    private val pbCardView by lazy { findViewById<ProgressBar>(R.id.progressBar_save_address) }
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }
    private var isModifyingAddress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_address)

        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            val addressToModify = userLoggedIn.addresses[intent.extras!!.getInt("position_list")]

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
