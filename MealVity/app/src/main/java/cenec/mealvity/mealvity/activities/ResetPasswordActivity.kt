package cenec.mealvity.mealvity.activities

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
import cenec.darash.mealvity.databinding.ActivityResetPasswordBinding
import cenec.darash.mealvity.databinding.ActivitySaveAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * Activity where the user can reset the account's password
 */
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding // View binding of the activity
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupViews()
    }

    /**
     * Sets up views for the activity
     */
    private fun setupViews() {
        binding.buttonResetPassword.setOnClickListener {
            resetPassword()
        }
    }

    /**
     * Function where an email is sent for the user to recover the account's password
     */
    private fun resetPassword() {
        binding.textviewNoExistingEmail.visibility=View.INVISIBLE
        binding.textviewResetPassword.visibility=View.GONE
        binding.progressBarResetPassword.visibility=View.VISIBLE
        val recoveryEmail = binding.editTextRecoveryEmail.text.toString()
        when {
            recoveryEmail.isEmpty() -> {
                binding.editTextRecoveryEmail.error = getString(R.string.text_field_empty)
                binding.editTextRecoveryEmail.requestFocus()
            }
            else -> {
                mFirebaseAuth.sendPasswordResetEmail(recoveryEmail).
                    addOnCompleteListener{task ->
                        binding.textviewResetPassword.visibility=View.VISIBLE
                        binding.progressBarResetPassword.visibility=View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(this@ResetPasswordActivity, "E-mail sent", Toast.LENGTH_LONG).show()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (userInvalid: FirebaseAuthInvalidUserException) {
                                binding.textviewNoExistingEmail.visibility=View.VISIBLE
                            } catch (emailInvalid: FirebaseAuthInvalidCredentialsException) {
                                binding.editTextRecoveryEmail.error=getString(R.string.text_email_invalid)
                                binding.editTextRecoveryEmail.requestFocus()
                            }
                        }
                    }
            }
        }
    }
}
