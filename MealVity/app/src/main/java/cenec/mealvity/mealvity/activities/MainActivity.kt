package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.user.Address
import cenec.mealvity.mealvity.classes.user.Order
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

/**
 * Main activity of the app, where the user can sign in or sign up
 */
class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient // Client used for interacting with the Google Sign In API
    private val RC_GOOGLE_SIGN_IN = 123 // Request code for signing in with Google
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Firebase Authentication
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() } // Instance of Firebase Firestore
    private val bSignInEmail by lazy { findViewById<Button>(R.id.button_email) } // Button for signing in with email/password
    private val bSignInGoogle by lazy { findViewById<Button>(R.id.button_google) } // Button for signing in with Google
    private val bSignUp by lazy { findViewById<TextView>(R.id.button_sign_up) } // Button for signing up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transitionFromSplashScreen()
        setupViews()
    }

    /**
     * Sets up views for the activity
     */
    private fun setupViews() {
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this, gso)

        bSignInEmail.setOnClickListener {
            emailSignIn()
        }

        bSignUp.setOnClickListener{
            createNewAccount()
        }

        bSignInGoogle.setOnClickListener {
            googleSignIn()
        }
    }

    /**
     * Launches the activity to sign in using email and password
     */
    private fun emailSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    /**
     * Launches the activity to create a new account
     */
    private fun createNewAccount() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    /**
     * Function launching the intent for signing in with google
     */
    private fun googleSignIn() {
        val intentGoogleSignIn=googleSignInClient.signInIntent
        startActivityForResult(intentGoogleSignIn, RC_GOOGLE_SIGN_IN)
    }

    /**
     * Function fetching the data from the intent.
     * From there, it will try to authenticate
     */
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

    /**
     * Function for authenticating using a Google account
     * @param googleAccount The google account to authenticate
     */
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

    /**
     * Function checking whether the recently signed in user exists in Firestore
     * @param currentUser Signed in Firebase User
     */
    private fun checkUserInDatabase(currentUser: FirebaseUser) {
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUser.uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If the user exists, it will go to the loading activity
                    // If not, it will first add the user to the Firestore and then it will proceed
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

    /**
     * Adds the new user to the Firesotre database
     * @param currentUser The new user
     */
    private fun addUserToDatabase(currentUser: FirebaseUser) {
        // To add an object to Firestore, we can add it as a hashmap value, since it uses a key-value pairing
        val currentFirebaseUser = mFirebaseAuth.currentUser
        val newUser = hashMapOf(
            Database.FIRESTORE_KEY_DATABASE_USERS_USER_ID to currentFirebaseUser!!.uid,
            Database.FIRESTORE_KEY_DATABASE_USERS_FULL_NAME to currentUser.displayName,
            Database.FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER to "",
            Database.FIRESTORE_KEY_DATABASE_USERS_EMAIL to currentUser.email,
            Database.FIRESTORE_KEY_DATABASE_USERS_ORDERS to arrayListOf<Order>(),
            Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES to arrayListOf<Address>()
        )
        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUser.uid).set(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Once the user has been added, it will proceed to the loading activity
                    goToLoadingActivity()
                } else {
                    Toast.makeText(this@MainActivity, "Error, please try again later", Toast.LENGTH_LONG).show()
                    Log.d("DebugUser", "${task.exception!!.message}")
                }
            }
    }

    /**
     * Launches the loading activity
     */
    private fun goToLoadingActivity() {
        val loadingActivity=Intent(this@MainActivity, LoadingActivity::class.java)
        startActivity(loadingActivity)
    }

    /**
     * Implements a trasition coming from the splash screen activity
     */
    private fun transitionFromSplashScreen() {
        window.sharedElementEnterTransition.duration=1000
        val slideAnimation=Slide(Gravity.BOTTOM)
        slideAnimation.excludeTarget(android.R.id.statusBarBackground, true)
        slideAnimation.excludeTarget(android.R.id.navigationBarBackground, true)
        slideAnimation.duration=1250
        slideAnimation.interpolator=AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        window.enterTransition=slideAnimation
    }

    /**
     * Overrides the default onBackPressed method
     * when we press back, we close the activity
     */
    override fun onBackPressed() {
        finishAffinity()
    }
}
