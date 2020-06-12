package cenec.mealvity.mealvity.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityOrderInfoBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import cenec.mealvity.mealvity.classes.user.Order
import java.util.*

class OrderInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderInfoBinding
    private val orderInfo by lazy { OrderSingleton.getInstance().getOrder() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCardViewPaymentBill()
        setupCardViewDeliveyDetails()
        setupCardViewOrderConfirmation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupCardViewPaymentBill() {
        setupCartItemRecyclerView()

        binding.cardViewPaymentBill.textViewBasePrice.text = String.format(Locale.getDefault(), "€%.2f", orderInfo.orderCart!!.getBasePrice())
        binding.cardViewPaymentBill.textViewTaxPrice.text = String.format(Locale.getDefault(), "€%.2f", orderInfo.orderCart!!.getTaxPrice())
        binding.cardViewPaymentBill.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", orderInfo.orderCart!!.totalPrice)
    }

    private fun setupCartItemRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(orderInfo.orderCart!!)

        binding.cardViewPaymentBill.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding.cardViewPaymentBill.recyclerViewOrderCart.adapter = rvAdapter
    }

    private fun setupCardViewDeliveyDetails() {
        binding.cardViewDeliveryDetails.textViewRestaurantName.text = "Restaurant: ${orderInfo.restaurantName}"
        binding.cardViewDeliveryDetails.textViewPaymentMethod.text = "Payment made by: ${orderInfo.paymentMethod}"
        binding.cardViewDeliveryDetails.textViewDeliveryMode.text = "Delivery Mode: ${orderInfo.deliveryMode}"

        if (orderInfo.deliveryMode == Order.DeliveryMode.PICKUP) {
            binding.cardViewDeliveryDetails.textViewDeliveryAddress.visibility = View.GONE
        } else {
            binding.cardViewDeliveryDetails.textViewDeliveryAddress.text = "Delivery Address: ${orderInfo.addressSelected}"
        }
    }

    private fun setupCardViewOrderConfirmation() {
        binding.cardViewOrderConfirmation.textViewOrderStatus.text = "${orderInfo.orderStatus}"
        binding.cardViewOrderConfirmation.textViewOrderStatus.setTextColor(setupOrderStatusColor())

        if (orderInfo.orderStatus == Order.OrderStatus.REJECTED) {
            showRejectionReasonLayout()
        } else if (orderInfo.orderStatus == Order.OrderStatus.ACCEPTED) {
            showDeliveryTime()
        }
    }

    private fun setupOrderStatusColor(): Int {
        return when (orderInfo.orderStatus) {
            Order.OrderStatus.ACCEPTED -> getColor(R.color.green_accept)
            Order.OrderStatus.REJECTED -> getColor(R.color.red_reject)
            else -> getColor(R.color.black)
        }
    }

    private fun showRejectionReasonLayout() {
        binding.cardViewOrderConfirmation.textViewRejectionReason.visibility = View.VISIBLE
        binding.cardViewOrderConfirmation.textViewRejectionReason.text = "Reason: ${orderInfo.rejectionReason}"
    }

    private fun showDeliveryTime() {
        binding.cardViewOrderConfirmation.textViewOrderExpectedTime.visibility = View.VISIBLE
        binding.cardViewOrderConfirmation.textViewOrderExpectedTime.text = "Expected delivery time: ${orderInfo.expectedDeliveryTime}"
    }
}
