package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import cenec.darash.mealvity.R
import com.google.firebase.auth.*

class ChangePasswordActivity : AppCompatActivity() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val etOldPassword by lazy { findViewById<EditText>(R.id.editText_old_password) }
    private val etNewPassword by lazy { findViewById<EditText>(R.id.editText_new_password) }
    private val etRepeatNewPassword by lazy { findViewById<EditText>(R.id.editText_repeat_new_password) }
    private val tvChangePassword by lazy { findViewById<TextView>(R.id.textView_change_password) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar_change_password) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    fun saveNewPassword(view: View) {
        val oldPassword = etOldPassword.text.toString()
        val newPassword = etNewPassword.text.toString()
        val repeatNewPassword = etRepeatNewPassword.text.toString()

        when {
            oldPassword.isEmpty() -> {
                etOldPassword.error=getString(R.string.text_field_empty)
                etOldPassword.requestFocus()
            }
            newPassword.isEmpty() -> {
                etNewPassword.error=getString(R.string.text_field_empty)
                etNewPassword.requestFocus()
            }
            repeatNewPassword.isEmpty() -> {
                etRepeatNewPassword.error=getString(R.string.text_field_empty)
                etNewPassword.requestFocus()
            }
            newPassword != repeatNewPassword -> {
                etRepeatNewPassword.error=getString(R.string.text_password_not_equal)
                etRepeatNewPassword.requestFocus()
            }
            oldPassword == newPassword -> {
                etNewPassword.error=getString(R.string.text_old_new_password_equal)
                etNewPassword.requestFocus()
            }
            else -> {
                progressBar.visibility=View.VISIBLE
                tvChangePassword.visibility=View.GONE
                val userSignedIn = mFirebaseAuth.currentUser
                val credential = EmailAuthProvider.getCredential(userSignedIn!!.email!!, oldPassword)

                userSignedIn.reauthenticate(credential)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful) {
                            userSignedIn.updatePassword(newPassword)
                                .addOnCompleteListener{ taskChangePassword ->
                                    progressBar.visibility=View.GONE
                                    tvChangePassword.visibility=View.VISIBLE
                                    if (taskChangePassword.isSuccessful) {
                                        Toast.makeText(this@ChangePasswordActivity, "Password changed", Toast.LENGTH_SHORT).show()
                                        finish()
                                    } else {
                                        //Toast.makeText(this@ChangePasswordActivity, taskChangePassword.exception!!.toString(), Toast.LENGTH_LONG).show()
                                        try {
                                            throw taskChangePassword.exception!!
                                        } catch (weakPasswordExcetion: FirebaseAuthWeakPasswordException) {
                                            etNewPassword.error=getString(R.string.text_weak_password)
                                            etNewPassword.requestFocus()
                                        }
                                    }
                                }
                        } else {
                            progressBar.visibility=View.GONE
                            tvChangePassword.visibility=View.VISIBLE
                            try {
                                throw task.exception!!
                            } catch (invalidCredentialsException: FirebaseAuthInvalidCredentialsException) {
                                etOldPassword.error=getString(R.string.text_invalid_password)
                                etOldPassword.requestFocus()
                            }
                        }
                    }
            }
        }
    }
}