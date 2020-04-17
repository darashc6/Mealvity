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

class LoadingActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(mFirebaseAuth.currentUser!!.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userLoggedIn=task.result!!.toObject(User::class.java)!!
                    UserSingleton.getInstance().setCurrentUser(userLoggedIn)
                    Toast.makeText(this, "Welcome to MealVity, ${userLoggedIn.fullName}!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }

        val handler=Handler()
        handler.postDelayed(Runnable {
            val intentHomePage=Intent(this, FragmentContainerActivity::class.java)
            intentHomePage.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentHomePage)
        }, 2000)
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
