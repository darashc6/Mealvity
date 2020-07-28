package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityHelpBinding
import cenec.mealvity.mealvity.classes.adapters.HelpRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.help.Help

/**
 * Class containing a series of questions and answers related to the application
 */
class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var listQuestions: ArrayList<Help>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListQuestions()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Help"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListQuestions() {
        listQuestions = arrayListOf()

        listQuestions.add(Help("What is MealVity?", "MealVity is an online food order and delivery service that acts as an intermediary between independent food outlets and our customers. We try to partner with the best restaurants in the industry, from small shops to international restaurants."))
        listQuestions.add(Help("How does MealVity work?", "The only thing you have to do is add your home address to find the best restaurants near you. From there, you can either reserve a table or order your food. Once you've made your reservations/order, it will notify the restaurant and act accordingly."))
        listQuestions.add(Help("How does a table reservation work?", "Once you select a restaurant near you, you can reserve a table providing the date of reservation, time of reservation and the number of guests. Once booked, it will notify the restaurant, and you will receive a status update in the app. In case your reservation gets rejected, the restaurant will provide you with a valid reason for rejection."))
        listQuestions.add(Help("How do you place an order?", "Once you select a restaurant near you, you can select from a vast number of items available from the restaurants' menu. Once you've done selecting your items, you need to fill out your details, select a delivery method (Pick-up/Home delivery) and select a payment method (Cash/Credit Card/Paypal)."))
    }

    private fun setupRecyclerView() {
        val rvAdapter = HelpRecyclerViewAdapter(listQuestions)
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewQuestionsList.layoutManager = rvLayoutManager
        binding.recyclerViewQuestionsList.adapter = rvAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
