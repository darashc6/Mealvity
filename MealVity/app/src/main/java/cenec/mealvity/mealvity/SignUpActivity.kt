package cenec.mealvity.mealvity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import cenec.darash.mealvity.R

class SignUpActivity : AppCompatActivity() {
    private val etFullName by lazy { findViewById<EditText>(R.id.editText_full_name) }
    private val etEmail by lazy { findViewById<EditText>(R.id.editText_email) }
    private val etPasswrd by lazy { findViewById<EditText>(R.id.editText_password) }
    private val etRepeatPassword by lazy { findViewById<EditText>(R.id.editText_password_repeat) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun emailSignIn(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun newAccountMealVity(view: View) {}
}
