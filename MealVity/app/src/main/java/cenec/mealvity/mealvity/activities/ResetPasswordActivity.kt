package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import cenec.darash.mealvity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class ResetPasswordActivity : AppCompatActivity() {
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_recovery_email) }
    private val tvResetPassword by lazy { findViewById<TextView>(R.id.textview_reset_password) }
    private val pbResetPassword by lazy { findViewById<ProgressBar>(R.id.progressBar_reset_password) }
    private val tvError by lazy { findViewById<TextView>(R.id.textview_no_existing_email) }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
    }

    fun resetPassword(view: View) {
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
