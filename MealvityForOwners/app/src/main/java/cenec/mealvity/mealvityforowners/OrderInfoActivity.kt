package cenec.mealvity.mealvityforowners

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.mealvity.mealvityforowners.core.order.Order
import cenec.mealvity.mealvityforowners.core.order.adapter.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvityforowners.core.order.adapter.OrderSingleton
import cenec.mealvity.mealvityforowners.core.user.User
import cenec.mealvity.mealvityforowners.databinding.ActivityOrderInfoBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class OrderInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderInfoBinding
    private var newOrderStatus: Order.OrderStatus = Order.OrderStatus.ACCEPTED
    private lateinit var orderUser: User
    private val currentOrder by lazy { OrderSingleton.getInstance().getOrder() }
    private val dbRestaurant by lazy { RestaurantDatabaseSingleton.getInstance().getRestaurantDatabase() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCardViewOrder()
        setupCardViewUserDetails()
        setupCardViewDeliveryDetails()
        setupCardViewConfirmation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupCardViewOrder() {
        setupCartItemRecyclerView()

        binding.cardViewOrder.textViewBasePrice.text = String.format(Locale.getDefault(), "€%.2f", currentOrder.orderCart!!.getBasePrice())
        binding.cardViewOrder.textViewTaxPrice.text = String.format(Locale.getDefault(), "€%.2f", currentOrder.orderCart!!.getTaxPrice())
        binding.cardViewOrder.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", currentOrder.orderCart!!.totalPrice)
    }

    private fun setupCartItemRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(currentOrder.orderCart!!.orderCart)

        binding.cardViewOrder.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding.cardViewOrder.recyclerViewOrderCart.adapter = rvAdapter
    }

    private fun setupCardViewUserDetails() {
        val userDetails = currentOrder.user

        binding.cardViewUserDetails.textViewUserName.text = "Name: ${userDetails!!.fullName}"
        binding.cardViewUserDetails.textViewUserPhoneNumber.text = "Phone number: ${userDetails.phoneNumber}"
        binding.cardViewUserDetails.textViewUserEmail.text = "Email: ${userDetails.email}"
    }

    private fun setupCardViewDeliveryDetails() {
        binding.cardViewDeliveryDetails.textViewPaymentMethod.text = "Payment made by: ${currentOrder.paymentMethod}"
        binding.cardViewDeliveryDetails.textViewDeliveryMode.text = "Delivery Mode: ${currentOrder.deliveryMode}"
        binding.cardViewDeliveryDetails.textViewDeliveryAddress.text = "Delivery Address: ${currentOrder.addressSelected}"
        binding.cardViewDeliveryDetails.textViewDeliveryTime.text = "Time: ${currentOrder.expectedDeliveryTime}"
    }

    private fun setupCardViewConfirmation() {
        if (currentOrder.orderStatus == Order.OrderStatus.ACCEPTED || currentOrder.orderStatus == Order.OrderStatus.REJECTED) {
            binding.cardViewConfirmation.textViewStatusInfo.visibility = View.VISIBLE
            if (currentOrder.orderStatus == Order.OrderStatus.ACCEPTED) {
                binding.cardViewConfirmation.textViewStatusInfo.text = "Accepted"
            } else {
                binding.cardViewConfirmation.textViewStatusInfo.text = "Rejected. Reason: ${currentOrder.rejectionReason}"
            }
        } else {
            binding.cardViewConfirmation.layoutRejectionOptions.visibility = View.VISIBLE
            setupConfirmationOptions()
        }
    }

    private fun setupConfirmationOptions() {
        binding.cardViewConfirmation.buttonAccept.buttonAccept.setOnClickListener {
            currentOrder.orderStatus = newOrderStatus
            for (i in 0 until dbRestaurant.orders.size) {
                if (dbRestaurant.orders[i].referenceNumber == currentOrder.referenceNumber) {
                    dbRestaurant.orders[i] = currentOrder
                }
            }
            updateChanges()
        }

        binding.cardViewConfirmation.buttonReject.buttonReject.setOnClickListener {
            showRejectionReasonLayout()
        }

        binding.cardViewConfirmation.buttonConfirmRejection.buttonConfirmRejection.setOnClickListener {
            currentOrder.orderStatus = Order.OrderStatus.REJECTED
            currentOrder.rejectionReason = binding.cardViewConfirmation.editTextRejectionReason.text.toString()
            for (i in 0 until dbRestaurant.orders.size) {
                if (dbRestaurant.orders[i].referenceNumber == currentOrder.referenceNumber) {
                    dbRestaurant.orders[i] = currentOrder
                }
            }
            updateChanges()
        }
    }

    private fun showRejectionReasonLayout() {
        val layoutRejectionReason = binding.cardViewConfirmation.layoutRejectionReason

        if (layoutRejectionReason.visibility == View.GONE) {
            layoutRejectionReason.visibility = View.VISIBLE
        } else {
            layoutRejectionReason.visibility = View.GONE
        }
    }

    private fun updateChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val restaurantNameString = currentOrder.restaurantName.replace(" ", "")

        mFirebaseFirestore.collection("restaurants").document(restaurantNameString.toLowerCase(Locale.ROOT))
            .set(dbRestaurant).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getReservationUser()
                } else {
                    Toast.makeText(this, "Error in updateChanges()", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun getReservationUser() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("users").document(currentOrder.user!!.userId!!)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    orderUser = task.result!!.toObject(User::class.java)!!
                    updateUserDatabase()
                } else {
                    Toast.makeText(this, "Error in getReservationUser()", Toast.LENGTH_LONG)
                        .show()
                    println(task.exception)
                }
            }
    }

    private fun updateUserDatabase() {
        val userOrdersList = orderUser.orders
        for (i in 0 until userOrdersList.size) {
            if (userOrdersList[i].referenceNumber == currentOrder.referenceNumber) {
                userOrdersList[i] = currentOrder
            }
        }

        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("users").document(orderUser.userId!!)
            .update("orders", userOrdersList)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                } else {
                    Toast.makeText(this, "Error in updateUserDatabase()", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
