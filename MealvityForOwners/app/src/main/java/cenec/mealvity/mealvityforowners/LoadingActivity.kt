package cenec.mealvity.mealvityforowners

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Class acting as a loading screen
 */
class LoadingActivity : AppCompatActivity() {
    private var restaurantName: String? = null // Restaurant name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        checkBundleExtras()
        getRestaurantDatabase()
    }

    /**
     * Checks the bundle received from the previous activity
     */
    private fun checkBundleExtras() {
        intent.extras?.let {
            restaurantName = it.getString("userId")!!
        }
    }

    /**
     * Looks for the restaurant database with the given name
     */
    private fun getRestaurantDatabase() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("restaurants")
            .document(restaurantName!!).get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    if (task.result!!.exists()) { // If the restaurant has a database
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

    /**
     * Creates a new database in case the restaurant doesn't have one
     */
    private fun createRestaurantDatabase() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val newDbRestaurant = RestaurantDatabase(restaurantName!!)

        mFirebaseFirestore.collection("restaurants").document(restaurantName!!)
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

    /**
     * Goes to the next activity, containing all the reservations and orders list
     */
    private fun goToListActivity() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this, FragmentContainerActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}
