package cenec.mealvity.mealvityforowners

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cenec.mealvity.mealvityforowners.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userId: String
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.buttonSignIn.buttonSignIn.setOnClickListener {
            signInRestaurant()
        }
    }

    private fun signInRestaurant() {
        userId = binding.textViewUserName.text.toString()

        if (userId.isEmpty()) {
            binding.textViewUserName.requestFocus()
            binding.textViewUserName.error = getString(R.string.text_error_field_is_empty)
        } else {
            attemptSignIn(userId)
        }
    }

    private fun attemptSignIn(userId: String) {
        mFirebaseAuth.signInWithEmailAndPassword("${userId}@gmail.com", userId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToLoadingActivity()
                } else {
                    registerRestaurant(userId)
                }
            }
    }

    private fun registerRestaurant(userId: String) {
        mFirebaseAuth.createUserWithEmailAndPassword("${userId}@gmail.com", userId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToLoadingActivity()
                } else {
                    println(task.exception)
                }
            }
    }

    private fun goToLoadingActivity() {
        val intentLoading = Intent(this, LoadingActivity::class.java)
        intentLoading.putExtra("userId", userId)
        startActivity(intentLoading)
    }
}
