package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * Activity where the user can reset the account's password
 */
class ResetPasswordActivity : AppCompatActivity() {
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_recovery_email) } // EditText for the account's email
    private val tvResetPassword by lazy { findViewById<TextView>(R.id.textview_reset_password) } // TextView of the button (which is a CardView)
    private val pbResetPassword by lazy { findViewById<ProgressBar>(R.id.progressBar_reset_password) } // ProgressBar of the button (which is a CardView)
    private val bResetPassword by lazy { findViewById<CardView>(R.id.button_reset_password) } // Button to reset the account's password
    private val tvError by lazy { findViewById<TextView>(R.id.textview_no_existing_email) } // TextView with the error message
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        bResetPassword.setOnClickListener {
            resetPassword()
        }
    }

    /**
     * Function where an email is sent for the user to recover the account's password
     */
    private fun resetPassword() {
        tvError.visibility=View.INVISIBLE
        tvResetPassword.visibility=View.GONE
        pbResetPassword.visibility=View.VISIBLE
        val recoveryEmail = etEmail.text.toString()
        when {
            recoveryEmail.isEmpty() -> {
                etEmail.error = getString(R.string.text_field_empty)
                etEmail.requestFocus()
            }
            else -> {
                mFirebaseAuth.sendPasswordResetEmail(recoveryEmail).
                    addOnCompleteListener{task ->
                        tvResetPassword.visibility=View.VISIBLE
                        pbResetPassword.visibility=View.GONE
                        if (task.isSuccessful) {
                            Toast.makeText(this@ResetPasswordActivity, "E-mail sent", Toast.LENGTH_LONG).show()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (userInvalid: FirebaseAuthInvalidUserException) {
                                tvError.visibility=View.VISIBLE
                            } catch (emailInvalid: FirebaseAuthInvalidCredentialsException) {
                                etEmail.error=getString(R.string.text_email_invalid)
                                etEmail.requestFocus()
                            }
                        }
                    }
            }
        }
    }
}
