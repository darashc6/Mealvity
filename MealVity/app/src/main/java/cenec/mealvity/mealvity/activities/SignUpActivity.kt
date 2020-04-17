package cenec.mealvity.mealvity.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.Address
import cenec.mealvity.mealvity.classes.user.Orders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore


class SignUpActivity : AppCompatActivity() {
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) }
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) }
    private val etPassword by lazy { findViewById<EditText>(R.id.editText_password) }
    private val etRepeatPassword by lazy { findViewById<EditText>(R.id.editText_password_repeat) }
    private val etPhoneNumber by lazy { findViewById<EditText>(R.id.editText_phone_number) }
    private val pbSignUp by lazy { findViewById<ProgressBar>(R.id.progressBar_sign_up) }
    private val tvSignUp by lazy { findViewById<TextView>(R.id.textview_sign_up) }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun gotoSignInActivity(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun newAccount(view: View) {
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
