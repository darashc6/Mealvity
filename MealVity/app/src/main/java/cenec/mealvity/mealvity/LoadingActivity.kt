package cenec.mealvity.mealvity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import cenec.darash.mealvity.R

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val handler=Handler()
        handler.postDelayed(Runnable {
            startActivity(Intent(this, FragmentContainerActivity::class.java))
        }, 4000)
    }

    override fun onBackPressed() {
        // Do nothing
    }
}
