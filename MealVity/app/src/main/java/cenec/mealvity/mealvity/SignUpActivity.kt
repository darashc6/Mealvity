package cenec.mealvity.mealvity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.user.User
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
                val newUser=User(fullName, phoneNumber, email, password)
                mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        pbSignUp.visibility=View.GONE
                        tvSignUp.visibility=View.VISIBLE
                        if (task.isSuccessful) {
                            mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).
                                document(newUser.email!!).set(newUser) // 100% works, so no need to add a listener

                            Toast.makeText(this@SignUpActivity, "Account created successfully", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@SignUpActivity, LoadingActivity::class.java))
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
