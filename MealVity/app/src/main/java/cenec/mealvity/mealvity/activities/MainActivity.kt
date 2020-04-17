package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.Address
import cenec.mealvity.mealvity.classes.user.Orders
import cenec.mealvity.mealvity.classes.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_GOOGLE_SIGN_IN = 123
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.sharedElementEnterTransition.duration=1000
        val slideAnimation=Slide(Gravity.BOTTOM)
        slideAnimation.excludeTarget(android.R.id.statusBarBackground, true)
        slideAnimation.excludeTarget(android.R.id.navigationBarBackground, true)
        slideAnimation.duration=1250
        slideAnimation.interpolator=AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        window.enterTransition=slideAnimation

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this, gso)
    }

    fun emailSignIn(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun createNewAccount(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    fun googleSignIn(view: View) {
        val intentGoogleSignIn=googleSignInClient.signInIntent
        startActivityForResult(intentGoogleSignIn, RC_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                val taskSignIn=GoogleSignIn.getSignedInAccountFromIntent(data)
                val googleAccount=taskSignIn.result
                firebaseAuthWithGoogle(googleAccount!!)
            } catch (e: ApiException) {
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            } catch (runtimeExecution: RuntimeExecutionException) {
                // This exception happens when the user doesn't choose a google account from the Google account chooser
                Log.d("errorException", runtimeExecution.message!!)
            }
        }
    }

    private fun firebaseAuthWithGoogle(googleAccount: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserInDatabase(FirebaseAuth.getInstance().currentUser!!)
                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUserInDatabase(currentUser: FirebaseUser) {
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUser.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result!!.toObject(User::class.java)
                    if (result==null) {
                        addUserToDatabase(currentUser)
                    } else {
                        goToLoadingActivity()
                    }
                } else {
                    // TODO
                }
            }
    }

    private fun addUserToDatabase(currentUser: FirebaseUser) {
        val newUser = hashMapOf(
            Database.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME to currentUser.displayName,
            Database.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER to "",
            Database.FIRESTORE_KEY_DATABASE_USERS_EMAIL to currentUser.email,
            Database.FIRESTORE_KEY_DATABASE_USERS_ORDERS to arrayListOf<Orders>(),
            Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES to arrayListOf<Address>()
        )
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUser.uid).set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToLoadingActivity()
                } else {
                    Toast.makeText(this@MainActivity, "Error, please try again later", Toast.LENGTH_LONG).show()
                    Log.d("DebugUser", "${task.exception!!.message}")
                }
            }
    }

    private fun goToLoadingActivity() {
        val loadingActivity=Intent(this@MainActivity, LoadingActivity::class.java)
        startActivity(loadingActivity)
    }

    override fun onStart() {
        super.onStart()
        if (mFirebaseAuth.currentUser!=null) {
            val intentLoading=Intent(this, LoadingActivity::class.java)
            startActivity(intentLoading)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
