package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import cenec.darash.mealvity.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Splash Screen of the application
 */
class SplashScreenActivity : AppCompatActivity() {
    private val imageLogo by lazy { findViewById<ImageView>(R.id.mealvity_logo) } // Image of the application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        setupTransitions()
        afterSplashScreen()
    }

    /**
     * Sets up the transitions of the splash screen
     */
    private fun setupTransitions() {
        window.exitTransition=null
        window.sharedElementExitTransition.duration=1000
    }

    /**
     * Sets up functionalities after the Splash Screen
     */
    private fun afterSplashScreen() {
        val handler=Handler()
        handler.postDelayed({
            val firebaseUser = FirebaseAuth.getInstance().currentUser

            if (firebaseUser!=null) {
                val intentLoading=Intent(this, LoadingActivity::class.java)
                startActivity(intentLoading)
            } else {
                val intentMain=Intent(this, MainActivity::class.java)
                val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenActivity, imageLogo, imageLogo.transitionName)
                startActivity(intentMain, opt.toBundle())
            }
        }, 1750)
    }
}
