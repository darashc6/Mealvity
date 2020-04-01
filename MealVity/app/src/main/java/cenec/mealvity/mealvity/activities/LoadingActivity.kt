package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoadingActivity : AppCompatActivity() {
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(mFirebaseAuth.currentUser!!.email!!).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = task.result!!.toObject(User::class.java)!!
                } else {
                    // TODO
                }
            }

        val handler=Handler()
        handler.postDelayed(Runnable {
            val intentHomePage=Intent(this, FragmentContainerActivity::class.java)
            val bun=Bundle()
            bun.putSerializable(Constants.BUNDLE_KEY_CURRENT_USER, currentUser)
            intentHomePage.putExtras(bun)

            startActivity(intentHomePage)
        }, 4000)
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
