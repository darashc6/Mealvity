package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.*

/**
 * Activity where the user has the ability to change the account's passwords
 * This activity will only be visible to those who created an account using email & password
 */
class ChangePasswordActivity : AppCompatActivity() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase Authentication
    private lateinit var binding: ActivityChangePasswordBinding // View binding of the activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupToolbar()
        setupViews()
    }

    /**
     * Sets up the toolbar of the activity
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the views in the activity
     */
    private fun setupViews() {
        binding.buttonChangePassword.setOnClickListener {
            saveNewPassword()
        }
    }

    /**
     * Function where the new password will be saved & stored in the authentication part of Firebase
     */
    private fun saveNewPassword() {
        val oldPassword = binding.editTextOldPassword.text.toString()
        val newPassword = binding.editTextNewPassword.text.toString()
        val repeatNewPassword = binding.editTextRepeatNewPassword.text.toString()

        when {
            oldPassword.isEmpty() -> {
                binding.editTextOldPassword.error=getString(R.string.text_field_empty)
                binding.editTextOldPassword.requestFocus()
            }
            newPassword.isEmpty() -> {
                binding.editTextNewPassword.error=getString(R.string.text_field_empty)
                binding.editTextNewPassword.requestFocus()
            }
            repeatNewPassword.isEmpty() -> {
                binding.editTextRepeatNewPassword.error=getString(R.string.text_field_empty)
                binding.editTextRepeatNewPassword.requestFocus()
            }
            newPassword != repeatNewPassword -> {
                binding.editTextRepeatNewPassword.error=getString(R.string.text_password_not_equal)
                binding.editTextRepeatNewPassword.requestFocus()
            }
            oldPassword == newPassword -> {
                binding.editTextNewPassword.error=getString(R.string.text_old_new_password_equal)
                binding.editTextNewPassword.requestFocus()
            }
            else -> {
                binding.progressBarChangePassword.visibility=View.VISIBLE
                binding.textViewChangePassword.visibility=View.GONE
                val userSignedIn = mFirebaseAuth.currentUser
                val credential = EmailAuthProvider.getCredential(userSignedIn!!.email!!, oldPassword)

                // In order to change the password of the account, we will need re-authentication
                userSignedIn.reauthenticate(credential)
                    .addOnCompleteListener{task ->
                        // If re-authentication is succesfull, the password will be changed in the Firebase
                        if (task.isSuccessful) {
                            updatePassword(newPassword)
                        } else {
                            // If re-athentication is unsuccessfull, this means that the user has introduced the wrong password
                            binding.progressBarChangePassword.visibility=View.GONE
                            binding.textViewChangePassword.visibility=View.VISIBLE
                            try {
                                throw task.exception!!
                            } catch (invalidCredentialsException: FirebaseAuthInvalidCredentialsException) {
                                binding.editTextOldPassword.error=getString(R.string.text_invalid_password)
                                binding.editTextOldPassword.requestFocus()
                            }
                        }
                    }
            }
        }
    }

    /**
     * Function where the new password inserted is updated in the Firebase Authentication
     * @param newPassword New password to update
     */
    private fun updatePassword(newPassword: String) {
        val currentUser = mFirebaseAuth.currentUser
        currentUser!!.updatePassword(newPassword)
            .addOnCompleteListener{ taskChangePassword ->
                binding.progressBarChangePassword.visibility=View.GONE
                binding.textViewChangePassword.visibility=View.VISIBLE
                if (taskChangePassword.isSuccessful) {
                    Toast.makeText(this@ChangePasswordActivity, "Password changed", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    try {
                        throw taskChangePassword.exception!!
                    } catch (weakPasswordExcetion: FirebaseAuthWeakPasswordException) {
                        binding.editTextNewPassword.error=getString(R.string.text_weak_password)
                        binding.editTextNewPassword.requestFocus()
                    }
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return true
    }
}
