package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivitySignInBinding
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
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Authentication for Firebase
    private lateinit var binding: ActivitySignInBinding // View binding of the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupViews()
    }

    /**
     * Sets up the views in the activity
     */
    private fun setupViews() {
        binding.buttonLogIn.setOnClickListener {
            logInAccount()
        }

        binding.buttonSignUp.setOnClickListener{
            gotoSignUpActivity()
        }

        binding.buttonRecoverPassword.setOnClickListener {
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
        binding.textviewError.visibility=View.INVISIBLE
        val email = binding.editTextSignInEmail.text.toString()
        val password = binding.editTextSignInPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.editTextSignInEmail.error = getString(R.string.text_field_empty)
                binding.editTextSignInEmail.requestFocus()
            }
            password.isEmpty() -> {
                binding.editTextSignInPassword.error = getString(R.string.text_field_empty)
                binding.editTextSignInPassword.requestFocus()
            }
            else -> {
                binding.textviewSignIn.visibility=View.GONE
                binding.progressBarSignIn.visibility=View.VISIBLE
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {task ->
                        binding.textviewSignIn.visibility=View.VISIBLE
                        binding.progressBarSignIn.visibility=View.GONE
                        if (task.isSuccessful) {
                            val intentLoading=Intent(this@SignInActivity, LoadingActivity::class.java)
                            startActivity(intentLoading)
                        } else {
                            try {
                                throw task.exception!!
                            } catch (invalidUser: FirebaseAuthInvalidUserException) {
                                binding.textviewError.visibility=View.VISIBLE
                            } catch (emailException: FirebaseAuthInvalidCredentialsException) {
                                when (emailException.errorCode) {
                                    "ERROR_WRONG_PASSWORD" -> binding.textviewError.visibility=View.VISIBLE
                                    "ERROR_INVALID_EMAIL" -> {
                                        binding.editTextSignInEmail.error=getString(R.string.text_email_invalid)
                                        binding.editTextSignInEmail.requestFocus()
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}
