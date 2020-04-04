package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.dialogs.CancelSavedChangesDialog
import cenec.mealvity.mealvity.classes.dialogs.InsertPasswordDialog
import cenec.mealvity.mealvity.classes.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity(), CancelSavedChangesDialog.CscDialogListener, InsertPasswordDialog.InsertPasswordDialogListener {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) }
    private val etPhoneNumber by lazy { findViewById<EditText>(R.id.editText_phone_number) }
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) }
    private lateinit var oldUser: User
    private var valuesChanged = false
    private var emailValueChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        oldUser=Constants.currentUser!!.copy()

        var toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fullName = Constants.currentUser!!.fullName
        val email = Constants.currentUser!!.email
        val phoneNumber = Constants.currentUser!!.phoneNumber
        etFullName.setText(Constants.currentUser!!.fullName)
        etPhoneNumber.setText(Constants.currentUser!!.phoneNumber)
        etEmail.setText(Constants.currentUser!!.email)

        etFullName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                valuesChanged = fullName != etFullName.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })

        if (Constants.currentUser!!.email!!.contains("@gmail", true)) {
            etEmail.isFocusable=false
            etEmail.isEnabled=false
            etEmail.isCursorVisible=false
            etEmail.keyListener=null
            etEmail.background=null
        } else {
            etEmail.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    valuesChanged = email != etEmail.text.toString()
                    emailValueChanged = valuesChanged
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

            })
        }

        etPhoneNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                valuesChanged = phoneNumber != etPhoneNumber.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })
    }

    fun saveChanges(view: View) {
        if (valuesChanged) {
            Constants.currentUser!!.fullName = etFullName.text.toString()
            Constants.currentUser!!.phoneNumber = etPhoneNumber.text.toString()
            Constants.currentUser!!.email = etEmail.text.toString()
            Constants.currentUser!!.updateUserLiveData()
            if (emailValueChanged) {
                reAuthenticateUser()
            } else {
                updateUserInDatabase(Constants.currentUser!!)
            }
        } else {
            // TODO Do nothing
        }
    }

    private fun reAuthenticateUser() {
        val passwordDialog = InsertPasswordDialog(this)
        passwordDialog.show(supportFragmentManager, "")
    }

    private fun updateUserInDatabase(userToUpdate: User) {
        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(userToUpdate.email!!)
            .update(
                Constants.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME, userToUpdate.fullName!!,
                Constants.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER, userToUpdate.phoneNumber!!,
                Constants.FIRESTORE_KEY_DATABASE_USERS_EMAIL, userToUpdate.email!!
            )
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Error saving changes. Please try again later", Toast.LENGTH_LONG).show()
                    Log.d("dErrorUpdateUser", task.exception!!.message!!, task.exception)
                }
            }
    }

    fun logOut(view: View) {
        mFirebaseAuth.signOut()

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient=GoogleSignIn.getClient(this, gso)

        mGoogleSignInClient.signOut()
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Logged out from MealVity", Toast.LENGTH_SHORT).show()
                    val intentLogOut=Intent(this, MainActivity::class.java)
                    intentLogOut.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentLogOut)
                } else {
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (valuesChanged) {
                    val cancelChangesDialog=CancelSavedChangesDialog(this)
                    cancelChangesDialog.show(supportFragmentManager, "")
                } else {
                    cancelChanges()
                }
            }
        }
        return true
    }

    override fun cancelChanges() {
        finish()
    }

    override fun getInputPassword(password: String) {
        val userSignedIn = mFirebaseAuth.currentUser
        val userCredential = EmailAuthProvider.getCredential(userSignedIn!!.email!!, password)

        userSignedIn.reauthenticate(userCredential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    updateEmail(userSignedIn, Constants.currentUser!!.email!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun deleteUserFromDatabase(email: String) {
        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(email).delete()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    addUpdatedUserToDatabase(Constants.currentUser!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUpdatedUserToDatabase(updatedUser: User) {
        val user = hashMapOf(
            Constants.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME to updatedUser.fullName,
            Constants.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER to updatedUser.phoneNumber,
            Constants.FIRESTORE_KEY_DATABASE_USERS_EMAIL to updatedUser.email,
            Constants.FIRESTORE_KEY_DATABASE_USERS_PASSWORD to updatedUser.password,
            Constants.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES to updatedUser.addresses,
            Constants.FIRESTORE_KEY_DATABASE_USERS_ORDERS to updatedUser.orders
        )

        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(updatedUser.email!!).set(user)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@EditProfileActivity, "Changes saved!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateEmail(userSignedIn: FirebaseUser, email: String){
        userSignedIn.updateEmail(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    deleteUserFromDatabase(oldUser.email!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

}