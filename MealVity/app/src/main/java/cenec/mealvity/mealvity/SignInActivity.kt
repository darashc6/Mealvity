package cenec.mealvity.mealvity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.user.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class SignInActivity : AppCompatActivity() {
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_sign_in_email) }
    private val etPassword by lazy { findViewById<EditText>(R.id.editText_sign_in_password) }
    private val tvError by lazy { findViewById<TextView>(R.id.textview_error) }
    private val pbSignIn by lazy { findViewById<ProgressBar>(R.id.progressBar_sign_in) }
    private val tvSignIn by lazy { findViewById<TextView>(R.id.textview_sign_in) }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    fun gotoSignUpActivity(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun gotoResetPasswordActivity(view: View) {
        startActivity(Intent(this, ResetPasswordActivity::class.java))
    }

    fun logInAccount(view: View) {
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
                        if (task.isSuccessful) {
                            returnDisplayName(email)
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

    private fun returnDisplayName(email: String) {
        var fullName = ""
        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result!!.toObject(User::class.java)
                    if (user!=null) {
                        fullName = user.fullName!!
                        Toast.makeText(this@SignInActivity, "Welcome back to MealVity, ${fullName}!", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@SignInActivity, LoadingActivity::class.java))
                    }
                }
            }
    }
}
