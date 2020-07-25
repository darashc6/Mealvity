package cenec.mealvity.mealvityforowners

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cenec.mealvity.mealvityforowners.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * Class acting as the opening activity to the app
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // View binding for the activity
    private lateinit var restaurantName: String // Restaurant name
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    /**
     * Sets up the views in the activity
     */
    private fun setupViews() {
        binding.buttonSignIn.buttonSignIn.setOnClickListener {
            signInRestaurant()
        }
    }

    /**
     * Function where the sign in happens given the name
     */
    private fun signInRestaurant() {
        restaurantName = binding.textViewUserName.text.toString()

        if (restaurantName.isEmpty()) {
            binding.textViewUserName.requestFocus()
            binding.textViewUserName.error = getString(R.string.text_error_field_is_empty)
        } else {
            attemptSignIn(restaurantName)
        }
    }

    /**
     * Attemps to sign in with the given restaurant name
     */
    private fun attemptSignIn(userId: String) {
        mFirebaseAuth.signInWithEmailAndPassword("${userId}@gmail.com", userId)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { // If the restaurant exists in the database
                    goToLoadingActivity()
                } else {
                    registerRestaurant(userId)
                }
            }
    }

    /**
     * Registers the ner restaurant to the database
     */
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

    /**
     * Goes to he next activity, which is a loading screen
     */
    private fun goToLoadingActivity() {
        val intentLoading = Intent(this, LoadingActivity::class.java)
        intentLoading.putExtra("userId", restaurantName)
        startActivity(intentLoading)
    }
}
