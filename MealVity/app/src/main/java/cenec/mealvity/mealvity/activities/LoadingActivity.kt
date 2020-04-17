package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.models.UserModel
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoadingActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(mFirebaseAuth.currentUser!!.email!!).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UserModel.getInstance().setCurrentUser(task.result!!.toObject(User::class.java)!!)
                } else {
                    // TODO Error
                }
            }

        val handler=Handler()
        handler.postDelayed(Runnable {
            val intentHomePage=Intent(this, FragmentContainerActivity::class.java)
            startActivity(intentHomePage)
        }, 4000)
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
