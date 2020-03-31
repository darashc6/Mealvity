package cenec.mealvity.mealvity

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
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.user.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
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
                checkUserInDatabase(googleAccount!!)
            } catch (e: ApiException) {
                Toast.makeText(this, e.statusCode, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(googleAccount: GoogleSignInAccount) {
        val credential=GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Welcome back to MealVity, ${mFirebaseAuth.currentUser!!.displayName}!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@MainActivity, LoadingActivity::class.java))
                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkUserInDatabase(googleAccount: GoogleSignInAccount) {
        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(googleAccount.email!!).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result!!.toObject(User::class.java)
                    if (result==null) {
                        addUserToDatabase(googleAccount)
                    } else {
                        firebaseAuthWithGoogle(googleAccount)
                    }
                } else {

                }
            }
    }

    private fun addUserToDatabase(googleAccount: GoogleSignInAccount) {
        val newUser = User(googleAccount.displayName, "", googleAccount.email, "none")
        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(newUser.email!!).set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuthWithGoogle(googleAccount)
                } else {
                    Toast.makeText(this@MainActivity, "Error, please try again later", Toast.LENGTH_LONG).show()
                    Log.d("DebugUser", "${task.exception!!.message}")
                }
            }
    }

    override fun onStart() {
        super.onStart()
        /*if (mFirebaseAuth.currentUser!=null) {
            startActivity(Intent(this, LoadingActivity::class.java))
        }*/
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
