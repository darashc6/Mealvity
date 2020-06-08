package cenec.mealvity.mealvity.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivitySignUpBinding
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.Address
import cenec.mealvity.mealvity.classes.user.Order
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable


/**
 * Activity where the user creates a new account using email and password
 */
class SignUpActivity : AppCompatActivity() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Authentication for Firebase
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of Firesotre database
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupViews()
    }

    /**
     * Sets up the views in the activity
     */
    private fun setupViews() {
        binding.buttonNewAccount.setOnClickListener {
            newAccount()
        }

        binding.buttonSignIn.setOnClickListener {
            gotoSignInActivity()
        }
    }

    /**
     * Redirects to the signing in activity
     */
    private fun gotoSignInActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * Creates a new account and stores the user in the database
     */
    private fun newAccount() {
        val fullName = binding.editTextFullName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val passwordRepeat = binding.editTextPasswordRepeat.text.toString()
        val phoneNumber = binding.editTextPhoneNumber.text.toString()
        when {
            fullName.isEmpty() -> {
                binding.editTextFullName.error = getString(R.string.text_field_empty)
                binding.editTextFullName.requestFocus()
            }
            email.isEmpty() -> {
                binding.editTextEmail.error = getString(R.string.text_field_empty)
                binding.editTextEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextPassword.error = getString(R.string.text_field_empty)
                binding.editTextPassword.requestFocus()
            }
            passwordRepeat.isEmpty() -> {
                binding.editTextPasswordRepeat.error = getString(R.string.text_field_empty)
                binding.editTextPasswordRepeat.requestFocus()
            }
            phoneNumber.isEmpty() -> {
                binding.editTextPhoneNumber.error = getString(R.string.text_field_empty)
                binding.editTextPhoneNumber.requestFocus()
            }
            password != passwordRepeat -> {
                binding.editTextPasswordRepeat.error = getString(R.string.text_password_not_equal)
                binding.editTextPasswordRepeat.requestFocus()
            }
            else -> {
                binding.progressBarSignUp.visibility=View.VISIBLE
                binding.textviewSignUp.visibility=View.GONE
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        binding.progressBarSignUp.visibility=View.GONE
                        binding.textviewSignUp.visibility=View.VISIBLE
                        if (task.isSuccessful) {
                            val firebaseUser = mFirebaseAuth.currentUser
                            val newUser = hashMapOf(
                                Database.FIRESTORE_KEY_DATABASE_USERS_USER_ID to firebaseUser!!.uid,
                                Database.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME to fullName,
                                Database.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER to phoneNumber,
                                Database.FIRESTORE_KEY_DATABASE_USERS_EMAIL to email,
                                Database.FIRESTORE_KEY_DATABASE_USERS_ORDERS to arrayListOf<Order>(),
                                Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES to arrayListOf<Address>()
                            )

                            addNewUserToDatabase(firebaseUser!!, newUser)
                        } else {
                            try {
                                throw task.exception!!
                            } catch (passwordException: FirebaseAuthWeakPasswordException) {
                                binding.editTextPassword.error=getString(R.string.text_weak_password)
                                binding.editTextPassword.requestFocus()
                            } catch (emailException: FirebaseAuthInvalidCredentialsException) {
                                binding.editTextEmail.error=getString(R.string.text_email_invalid)
                                binding.editTextEmail.requestFocus()
                            } catch (existingUserException: FirebaseAuthUserCollisionException) {
                                binding.editTextEmail.error=getString(R.string.text_user_already_exists)
                                binding.editTextEmail.requestFocus()
                            }
                        }
                    }
            }
        }
    }

    /**
     * Adds the new user to the database
     * @param fUser New Firebase User
     * @param newUser Map containing the user's necessary details to add to the database
     */
    private fun addNewUserToDatabase(fUser: FirebaseUser, newUser: HashMap<String, Serializable>) {
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS)
            .document(fUser.uid).set(newUser)
            .addOnCompleteListener { databaseTask ->
                if (databaseTask.isSuccessful) {
                    val intentLoading=Intent(this@SignUpActivity, LoadingActivity::class.java)
                    startActivity(intentLoading)
                } else {
                    Toast.makeText(this, databaseTask.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
