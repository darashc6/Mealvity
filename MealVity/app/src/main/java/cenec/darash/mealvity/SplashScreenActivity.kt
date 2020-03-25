package cenec.darash.mealvity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat

class SplashScreenActivity : AppCompatActivity() {
    private val imageLogo by lazy { findViewById<ImageView>(R.id.mealvity_logo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        /*val fade=Fade()
        val decor=window.decorView
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container) as View?, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition=fade
        // window.exitTransition=fade*/


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
