package cenec.mealvity.mealvity.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.Address
import cenec.mealvity.mealvity.classes.user.Orders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore


/**
 * Activity where the user creates a new account using email and password
 */
class SignUpActivity : AppCompatActivity() {
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) } // EditText for the user's full name
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) } // EditText for the user's email
    private val etPassword by lazy { findViewById<EditText>(R.id.editText_password) } // EditText for the user's password
    private val etRepeatPassword by lazy { findViewById<EditText>(R.id.editText_password_repeat) } // EditText for introducing the password again
    private val etPhoneNumber by lazy { findViewById<EditText>(R.id.editText_phone_number) }  // EditText for the user's phone number
    private val pbSignUp by lazy { findViewById<ProgressBar>(R.id.progressBar_sign_up) } // ProgressBar for the button (which is a CardView)
    private val tvSignUp by lazy { findViewById<TextView>(R.id.textview_sign_up) } // TextView for the button (which is a CardView)
    private val bSignUp by lazy { findViewById<CardView>(R.id.button_new_account) } // Button for creating the new account
    private val bSignIn by lazy { findViewById<TextView>(R.id.button_sign_in) } // Button for signing in
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Authentication for Firebase
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of Firesotre database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        bSignUp.setOnClickListener {
            newAccount()
        }

        bSignIn.setOnClickListener {
            gotoSignInActivity()
        }
    }

    /**
     * Redirects to the signing in activity
     */
    private fun gotoSignInActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    /**
     * Creates a new account and stores the user in the database
     */
    private fun newAccount() {
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val passwordRepeat = etRepeatPassword.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()
        when {
            fullName.isEmpty() -> {
                etFullName.error = getString(R.string.text_field_empty)
                etFullName.requestFocus()
            }
            email.isEmpty() -> {
                etEmail.error = getString(R.string.text_field_empty)
                etEmail.requestFocus()
            }
            password.isEmpty() -> {
                etPassword.error = getString(R.string.text_field_empty)
                etPassword.requestFocus()
            }
            passwordRepeat.isEmpty() -> {
                etRepeatPassword.error = getString(R.string.text_field_empty)
                etRepeatPassword.requestFocus()
            }
            phoneNumber.isEmpty() -> {
                etPhoneNumber.error = getString(R.string.text_field_empty)
                etPhoneNumber.requestFocus()
            }
            password != passwordRepeat -> {
                etRepeatPassword.error = getString(R.string.text_password_not_equal)
                etRepeatPassword.requestFocus()
            }
            else -> {
                pbSignUp.visibility=View.VISIBLE
                tvSignUp.visibility=View.GONE
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        pbSignUp.visibility=View.GONE
                        tvSignUp.visibility=View.VISIBLE
                        if (task.isSuccessful) {
                            val firebaseUser = mFirebaseAuth.currentUser
                            val newUser = hashMapOf(
                                Database.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME to fullName,
                                Database.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER to phoneNumber,
                                Database.FIRESTORE_KEY_DATABASE_USERS_EMAIL to email,
                                Database.FIRESTORE_KEY_DATABASE_USERS_ORDERS to arrayListOf<Orders>(),
                                Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES to arrayListOf<Address>()
                            )

                            mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).
                                document(firebaseUser!!.uid).set(newUser)
                                .addOnCompleteListener { databaseTask ->
                                    if (databaseTask.isSuccessful) {
                                        val intentLoading=Intent(this@SignUpActivity, LoadingActivity::class.java)
                                        startActivity(intentLoading)
                                    } else {
                                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            try {
                                throw task.exception!!
                            } catch (passwordException: FirebaseAuthWeakPasswordException) {
                                etPassword.error=getString(R.string.text_weak_password)
                                etPassword.requestFocus()
                            } catch (emailException: FirebaseAuthInvalidCredentialsException) {
                                etEmail.error=getString(R.string.text_email_invalid)
                                etEmail.requestFocus()
                            } catch (existingUserException: FirebaseAuthUserCollisionException) {
                                etEmail.error=getString(R.string.text_user_already_exists)
                                etEmail.requestFocus()
                            }
                        }
                    }
            }
        }
    }
}
