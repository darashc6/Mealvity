package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity simulating the loading time between one activity and the other
 */
class LoadingActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of Firebase Firestore
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase Auth
    private var userLoggedIn: User? = null // User logged in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        loadUserFromDatabase()
        goToHomeActivity()
    }

    /**
     * Loads the user from the database given the FirebaseAuth user's UID
     */
    private fun loadUserFromDatabase() {
        // We search for the user currently logged in via Firebase Auth
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(mFirebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLoggedIn=task.result!!.toObject(User::class.java)
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Launches the next activity (FragmentContainerActivity) if the user logged in exists in the database
     */
    private fun goToHomeActivity() {
        val handler=Handler()
        handler.postDelayed(Runnable {
            // If the user exists, userLoggedIn won't be null, and the app will move to the next activity (FragmentContainerActivity)
            if (userLoggedIn!=null) {
                UserSingleton.getInstance().setCurrentUser(userLoggedIn!!)
                Toast.makeText(this, "Welcome to MealVity, ${userLoggedIn!!.fullName}!", Toast.LENGTH_LONG).show()

                val intentHomePage=Intent(this, FragmentContainerActivity::class.java)
                intentHomePage.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentHomePage)
            } else {
                Toast.makeText(this, "Error trying to log in. Please try again later.", Toast.LENGTH_SHORT).show()
                val intentMainActivity=Intent(this, MainActivity::class.java)
                intentMainActivity.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intentMainActivity)
            }
        }, 3000)
    }

    /**
     * Overrides the back button
     */
    override fun onBackPressed() {
        // Do nothing
    }
}
