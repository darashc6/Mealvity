package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import cenec.darash.mealvity.R

class SplashScreenActivity : AppCompatActivity() {
    private val imageLogo by lazy { findViewById<ImageView>(R.id.mealvity_logo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.exitTransition=null
        window.sharedElementExitTransition.duration=1000

        val handler=Handler()
        handler.postDelayed({
            val intentMain=Intent(this, MainActivity::class.java)
            val opt = ActivityOptionsCompat.makeSceneTransitionAnimation(this@SplashScreenActivity, imageLogo, imageLogo.transitionName)
            startActivity(intentMain, opt.toBundle())
        }, 1750)
    }
}
