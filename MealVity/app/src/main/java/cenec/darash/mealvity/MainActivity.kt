package cenec.darash.mealvity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView

class MainActivity : AppCompatActivity() {

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
    }

    fun emailSignIn(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
    }

    fun createNewAccount(view: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
