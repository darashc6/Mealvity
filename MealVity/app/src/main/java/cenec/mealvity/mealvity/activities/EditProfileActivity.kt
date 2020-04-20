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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.dialogs.CancelSavedChangesDialog
import cenec.mealvity.mealvity.classes.dialogs.InsertPasswordDialog
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity where the user can edit the main aspects of his profile (Name, phone number, email)
 * The user can change his email if he created his account using email and password
 * This Activity implements 2 interfaces:
 * CscDialogListener -> From CancelSavedChangesDialog
 * InsertPasswordDialogListener -> From InsertPasswordDialog
 */
class EditProfileActivity : AppCompatActivity(), CancelSavedChangesDialog.CscDialogListener, InsertPasswordDialog.InsertPasswordDialogListener {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase Authentication
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of Firebase Firestore
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // The user currently logged in (returns an User)
    private lateinit var mGoogleSignInClient: GoogleSignInClient // Client interacting with the Google Sign In API
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) } // EditText for the user's full name
    private val etPhoneNumber by lazy { findViewById<EditText>(R.id.editText_phone_number) } // EditText for the user's phone number
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) } // EditText for the use'rs email
    private val tvSaveChanges by lazy { findViewById<TextView>(R.id.textview_save_changes) } // TextView of the button (which is a CardView)
    private val pbSaveChanges by lazy { findViewById<ProgressBar>(R.id.progressBar_save_changes) } // ProgressBar of the button (which is a CardView)
    private val bSaveChanges by lazy { findViewById<CardView>(R.id.button_save_changes) } // Button used for saving changes
    private val bLogOut by lazy { findViewById<CardView>(R.id.button_log_out) } // Button used for logging out of the app
    private lateinit var oldUser: User // Copy of the User before saving his changes
    private var valuesChanged = false // True if the EditTexts have been modified, false if otherwise
    private var emailValueChanged = false // True if the email has been modified, false if otherwise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        oldUser=userLoggedIn.copy()

        var toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fullName = userLoggedIn.fullName
        val email = userLoggedIn.email
        val phoneNumber = userLoggedIn.phoneNumber
        etFullName.setText(userLoggedIn.fullName)
        etPhoneNumber.setText(userLoggedIn.phoneNumber)
        etEmail.setText(userLoggedIn.email)

        // As soon as there is any change in the EditText, valuesChanged will be true
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

        // If user has created an account using Google, he won't be a ble to modify his email
        if (mFirebaseAuth.currentUser!!.getIdToken(false).result!!.signInProvider.equals("google.com")) {
            etEmail.isFocusable=false
            etEmail.isEnabled=false
            etEmail.isCursorVisible=false
            etEmail.keyListener=null
            etEmail.background=null
        } else {
            // As soon as there is any change in the EditText, valuesChanged (and emailValueChanged in the case) will be true
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

        // As soon as there is any change in the EditText, valuesChanged will be true
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

        bSaveChanges.setOnClickListener {
            saveChanges()
        }

        bLogOut.setOnClickListener {
            logOut()
        }
    }

    /**
     * Function where the changes will be saved and stored in Firebase (if the user has made them)
     */
    private fun saveChanges() {
        if (valuesChanged) {
            tvSaveChanges.visibility=View.GONE
            pbSaveChanges.visibility=View.VISIBLE
            // If the email has been changed, re-authentication of the user is needed.
            // For that to happen, we will ask the user to provide his password
            if (emailValueChanged) {
                reAuthenticateUser()
            } else {
                userLoggedIn.fullName = etFullName.text.toString()
                userLoggedIn.phoneNumber = etPhoneNumber.text.toString()
                updateUserInDatabase(userLoggedIn)
            }
        } else {
            Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * Function where a dialog will pop up to ask for his password
     */
    private fun reAuthenticateUser() {
        val passwordDialog = InsertPasswordDialog(this)
        passwordDialog.show(supportFragmentManager, "")
    }

    /**
     * Function where the email will be updated and stored in the authentication part of Firebase
     * This means that authentication was successfull and the change was able to happen
     */
    private fun updateEmail(fUser: FirebaseUser, email: String){
        fUser.updateEmail(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    userLoggedIn.fullName = etFullName.text.toString()
                    userLoggedIn.phoneNumber = etPhoneNumber.text.toString()
                    userLoggedIn.email = etEmail.text.toString()
                    updateUserInDatabase(userLoggedIn)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Function where the changes made to the User will be updated to the database part of Firebase
     * @param userToUpdate User to update in Firestore
     */
    private fun updateUserInDatabase(userToUpdate: User) {
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(mFirebaseAuth.currentUser!!.uid)
            .update(
                Database.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME, userToUpdate.fullName!!,
                Database.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER, userToUpdate.phoneNumber!!,
                Database.FIRESTORE_KEY_DATABASE_USERS_EMAIL, userToUpdate.email!!
            )
            .addOnCompleteListener{task ->
                tvSaveChanges.visibility=View.VISIBLE
                pbSaveChanges.visibility=View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Changes saved!", Toast.LENGTH_SHORT).show()
                    UserSingleton.getInstance().setCurrentUser(userToUpdate)
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Error saving changes. Please try again later", Toast.LENGTH_LONG).show()
                    Log.d("dErrorUpdateUser", task.exception!!.message!!, task.exception)
                }
            }
    }

    /**
     * Function used to log out from the application
     */
    private fun logOut() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient=GoogleSignIn.getClient(this, gso)

        mFirebaseAuth.signOut()
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

    /**
     * Function overriding the back button on the toolbar
     * When the back button is pressed, a dialog will show asking whether the user would want to cancel the saved changes, if the are any
     * If there aren't any saved changes, the activity will finish
     */
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

    /**
     * Function implemented using the listener in CancelSavedDialog
     * It is implemented in the onOptionsItemSelected
     */
    override fun cancelChanges() {
        finish()
    }

    /**
     * Function implemented using the listener in InsertPasswordDialog
     * It will ask for the user's password to re-authenticate
     */
    override fun getInputPassword(password: String) {
        val userSignedIn = mFirebaseAuth.currentUser
        val userCredential = EmailAuthProvider.getCredential(userSignedIn!!.email!!, password)

        userSignedIn.reauthenticate(userCredential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    updateEmail(userSignedIn, userLoggedIn.email!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

}
