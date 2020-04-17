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

class EditProfileActivity : AppCompatActivity(), CancelSavedChangesDialog.CscDialogListener, InsertPasswordDialog.InsertPasswordDialogListener {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() }
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) }
    private val etPhoneNumber by lazy { findViewById<EditText>(R.id.editText_phone_number) }
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) }
    private val tvSaveChanges by lazy { findViewById<TextView>(R.id.textview_save_changes) }
    private val pbSaveChanges by lazy { findViewById<ProgressBar>(R.id.progressBar_save_changes) }
    private lateinit var oldUser: User
    private var valuesChanged = false
    private var emailValueChanged = false

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

        if (userLoggedIn.email!!.contains("@gmail", true)) {
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
            tvSaveChanges.visibility=View.GONE
            pbSaveChanges.visibility=View.VISIBLE
            userLoggedIn.fullName = etFullName.text.toString()
            userLoggedIn.phoneNumber = etPhoneNumber.text.toString()
            userLoggedIn.email = etEmail.text.toString()
            if (emailValueChanged) {
                reAuthenticateUser()
            } else {
                updateUserInDatabase(userLoggedIn)
            }
        } else {
            // TODO Do nothing
        }
    }

    private fun reAuthenticateUser() {
        val passwordDialog = InsertPasswordDialog(this)
        passwordDialog.show(supportFragmentManager, "")
    }

    private fun updateEmail(fUser: FirebaseUser, email: String){
        fUser.updateEmail(email)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    updateUserInDatabase(userLoggedIn)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

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
                    updateEmail(userSignedIn, userLoggedIn.email!!)
                } else {
                    Toast.makeText(this, task.exception!!.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

}
