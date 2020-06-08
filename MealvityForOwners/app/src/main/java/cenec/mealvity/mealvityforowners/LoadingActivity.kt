package cenec.mealvity.mealvityforowners

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LoadingActivity : AppCompatActivity() {
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        checkBundleExtras()
        getRestaurantDatabase()
    }

    private fun checkBundleExtras() {
        intent.extras?.let {
            userId = it.getString("userId")!!
        }
    }

    private fun getRestaurantDatabase() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("restaurants")
            .document(userId!!).get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    if (task.result!!.exists()) {
                        val dbRestaurant = task.result!!.toObject(RestaurantDatabase::class.java)
                        RestaurantDatabaseSingleton.getInstance().setRestaurantDatabase(dbRestaurant!!)
                        goToListActivity()
                    } else {
                        createRestaurantDatabase()
                    }
                } else {
                    Toast.makeText(this, "Error loading", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun createRestaurantDatabase() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val newDbRestaurant = RestaurantDatabase(userId!!)

        mFirebaseFirestore.collection("restaurants").document(userId!!)
            .set(newDbRestaurant)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    RestaurantDatabaseSingleton.getInstance().setRestaurantDatabase(newDbRestaurant)
                    goToListActivity()
                } else {
                    Toast.makeText(this, "Error creating", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun goToListActivity() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, FragmentContainerActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}
