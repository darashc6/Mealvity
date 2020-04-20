package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity where the user authenticates using email and password
 */
class SignInActivity : AppCompatActivity() {
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_sign_in_email) } // EditText for the email
    private val etPassword by lazy { findViewById<EditText>(R.id.editText_sign_in_password) } // EditText for the password
    private val tvError by lazy { findViewById<TextView>(R.id.textview_error) } // TextView of the error message
    private val pbSignIn by lazy { findViewById<ProgressBar>(R.id.progressBar_sign_in) } // ProgressBar of the button (which is a CardView)
    private val tvSignIn by lazy { findViewById<TextView>(R.id.textview_sign_in) } // TextView of the button (which is a CardView)
    private val bSignIn by lazy { findViewById<CardView>(R.id.button_log_in) } // Button for logging in
    private val bSignUp by lazy { findViewById<TextView>(R.id.button_sign_up) } // Button for creating a new account
    private val bRecoverPassword by lazy { findViewById<TextView>(R.id.button_recover_password) } // Button to recover password
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Authentication for Firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        bSignIn.setOnClickListener {
            logInAccount()
        }

        bSignUp.setOnClickListener{
            gotoSignUpActivity()
        }

        bRecoverPassword.setOnClickListener {
            gotoResetPasswordActivity()
        }
    }

    /**
     * Redirects to the signing up activity
     */
    private fun gotoSignUpActivity() {
        val intentSignUp=Intent(this, SignUpActivity::class.java)
        startActivity(intentSignUp)
    }

    /**
     * Redirects to the recovering password activity
     */
    private fun gotoResetPasswordActivity() {
        val intentRecoverPassword=Intent(this, ResetPasswordActivity::class.java)
        startActivity(intentRecoverPassword)
    }

    /**
     * Function where the authentication happens with the email and password provided
     */
    private fun logInAccount() {
        tvError.visibility=View.INVISIBLE
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        when {
            email.isEmpty() -> {
                etEmail.error = getString(R.string.text_field_empty)
                etEmail.requestFocus()
            }
            password.isEmpty() -> {
                etPassword.error = getString(R.string.text_field_empty)
                etPassword.requestFocus()
            }
            else -> {
                tvSignIn.visibility=View.GONE
                pbSignIn.visibility=View.VISIBLE
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        tvSignIn.visibility=View.VISIBLE
                        pbSignIn.visibility=View.GONE
                        if (task.isSuccessful) { // If
                            val intentLoading=Intent(this@SignInActivity, LoadingActivity::class.java)
                            startActivity(intentLoading)
                        } else {
                            try {
                                throw task.exception!!
                            } catch (invalidUser: FirebaseAuthInvalidUserException) {
                                tvError.visibility=View.VISIBLE
                            } catch (emailException: FirebaseAuthInvalidCredentialsException) {
                                when (emailException.errorCode) {
                                    "ERROR_WRONG_PASSWORD" -> tvError.visibility=View.VISIBLE
                                    "ERROR_INVALID_EMAIL" -> {
                                        etEmail.error=getString(R.string.text_email_invalid)
                                        etEmail.requestFocus()
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}
