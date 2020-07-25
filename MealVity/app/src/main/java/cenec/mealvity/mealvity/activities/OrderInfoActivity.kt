package cenec.mealvity.mealvity.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityOrderInfoBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import cenec.mealvity.mealvity.classes.user.Order
import java.util.*

/**
 * Activity showing all the info regarding an Order
 */
class OrderInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderInfoBinding // View binding of the activity
    private val order by lazy { OrderSingleton.getInstance().getOrder() } // Order selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCardViewPaymentBill()
        setupCardViewDeliveyDetails()
        setupCardViewOrderConfirmation()
    }

    /**
     * Sets up the activity's toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Sets up the payment bill CardView
     */
    private fun setupCardViewPaymentBill() {
        setupCartItemRecyclerView()

        binding.cardViewPaymentBill.textViewBasePrice.text = String.format(Locale.getDefault(), "€%.2f", order.orderCart!!.getBasePrice())
        binding.cardViewPaymentBill.textViewTaxPrice.text = String.format(Locale.getDefault(), "€%.2f", order.orderCart!!.getTaxPrice())
        binding.cardViewPaymentBill.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", order.orderCart!!.totalPrice)
    }

    /**
     * Sets up the RecyclerView for the order's cart
     */
    private fun setupCartItemRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(order.orderCart!!)

        binding.cardViewPaymentBill.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding.cardViewPaymentBill.recyclerViewOrderCart.adapter = rvAdapter
    }

    /**
     * Sets up the delivery details' CardView
     */
    private fun setupCardViewDeliveyDetails() {
        binding.cardViewDeliveryDetails.textViewRestaurantName.text = "Restaurant: ${order.restaurantName}"
        binding.cardViewDeliveryDetails.textViewPaymentMethod.text = "Payment made by: ${order.paymentMethod}"
        binding.cardViewDeliveryDetails.textViewDeliveryMode.text = "Delivery Mode: ${order.deliveryMode}"

        if (order.deliveryMode == Order.DeliveryMode.PICKUP) {
            binding.cardViewDeliveryDetails.textViewDeliveryAddress.visibility = View.GONE
        } else {
            binding.cardViewDeliveryDetails.textViewDeliveryAddress.text = "Delivery Address: ${order.addressSelected}"
        }
    }

    /**
     * Sets up the order confirmation's CardView
     */
    private fun setupCardViewOrderConfirmation() {
        binding.cardViewOrderConfirmation.textViewOrderStatus.text = "${order.orderStatus}"
        binding.cardViewOrderConfirmation.textViewOrderStatus.setTextColor(setupOrderStatusColor())

        if (order.orderStatus == Order.OrderStatus.REJECTED) {
            showRejectionReasonLayout()
        } else if (order.orderStatus == Order.OrderStatus.ACCEPTED) {
            showDeliveryTime()
        }
    }

    /**
     * Sets up the text color depending on the order status
     * @return Integer value of the color
     */
    private fun setupOrderStatusColor(): Int {
        return when (order.orderStatus) {
            Order.OrderStatus.ACCEPTED -> getColor(R.color.green_accept)
            Order.OrderStatus.REJECTED -> getColor(R.color.red_reject)
            else -> getColor(R.color.black)
        }
    }

    /**
     * Shows the order's rejection reason
     */
    private fun showRejectionReasonLayout() {
        binding.cardViewOrderConfirmation.textViewRejectionReason.visibility = View.VISIBLE
        binding.cardViewOrderConfirmation.textViewRejectionReason.text = "Reason: ${order.rejectionReason}"
    }

    /**
     * Shows the order's expected delivery time
     */
    private fun showDeliveryTime() {
        binding.cardViewOrderConfirmation.textViewOrderExpectedTime.visibility = View.VISIBLE
        binding.cardViewOrderConfirmation.textViewOrderExpectedTime.text = "Expected delivery time: ${order.expectedDeliveryTime}"
    }

    /**
     * Overrides buttons displayed in the toolbar
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
