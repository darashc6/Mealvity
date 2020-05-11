package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityEditProfileBinding
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
    private var valuesChanged = false // True if the EditTexts have been modified, false if otherwise
    private var emailValueChanged = false // True if the email has been modified, false if otherwise
    private lateinit var binding: ActivityEditProfileBinding // View binding of the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupToolbar()
        setupViews()
    }

    /**
     * Sets up the toolbar of the activity
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the views of the activity
     */
    private fun setupViews() {
        val oldFullName = userLoggedIn.fullName
        val oldPhoneNumber = userLoggedIn.phoneNumber
        val oldEmail = userLoggedIn.email

        binding.editTextFullName.setText(userLoggedIn.fullName)
        binding.editTextPhoneNumber.setText(userLoggedIn.phoneNumber)
        binding.editTextEmail.setText(userLoggedIn.email)

        // As soon as there is any change in the EditText, valuesChanged will be true
        binding.editTextFullName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                valuesChanged = oldFullName != binding.editTextFullName.text.toString()
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
            binding.editTextEmail.isFocusable=false
            binding.editTextEmail.isEnabled=false
            binding.editTextEmail.isCursorVisible=false
            binding.editTextEmail.keyListener=null
            binding.editTextEmail.background=null
        } else {
            // As soon as there is any change in the EditText, valuesChanged (and emailValueChanged in the case) will be true
            binding.editTextEmail.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    valuesChanged = oldEmail != binding.editTextEmail.text.toString()
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
        binding.editTextPhoneNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                valuesChanged = oldPhoneNumber != binding.editTextPhoneNumber.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

        })

        binding.buttonSaveChanges.setOnClickListener {
            saveChanges()
        }

        binding.buttonLogOut.setOnClickListener {
            logOut()
        }
    }

    /**
     * Function where the changes will be saved and stored in Firebase (if the user has made them)
     */
    private fun saveChanges() {
        if (valuesChanged) {
            binding.textviewSaveChanges.visibility=View.GONE
            binding.progressBarSaveChanges.visibility=View.VISIBLE
            // If the email has been changed, re-authentication of the user is needed.
            // For that to happen, we will ask the user to provide his password
            if (emailValueChanged) {
                reAuthenticateUser()
            } else {
                userLoggedIn.fullName = binding.editTextFullName.text.toString()
                userLoggedIn.phoneNumber = binding.editTextPhoneNumber.text.toString()
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
     * @param email updated Email
     */
    private fun updateEmail(email: String){
        mFirebaseAuth.currentUser!!.updateEmail(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    userLoggedIn.fullName = binding.editTextFullName.text.toString()
                    userLoggedIn.phoneNumber = binding.editTextPhoneNumber.text.toString()
                    userLoggedIn.email = binding.editTextEmail.text.toString()
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
                binding.textviewSaveChanges.visibility=View.VISIBLE
                binding.progressBarSaveChanges.visibility=View.GONE
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
     * @param password Password from the EditText
     */
    override fun getInputPassword(password: String) {
        val userSignedIn = mFirebaseAuth.currentUser
        val userCredential = EmailAuthProvider.getCredential(userSignedIn!!.email!!, password)

        userSignedIn.reauthenticate(userCredential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    updateEmail(userLoggedIn.email!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

}
