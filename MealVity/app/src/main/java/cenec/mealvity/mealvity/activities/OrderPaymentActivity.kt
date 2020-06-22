package cenec.mealvity.mealvity.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityPaymentBinding
import cenec.darash.mealvity.databinding.FragmentDeliveryBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.config.PaypalConfig
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter2
import cenec.mealvity.mealvity.classes.orders.OrderCart
import cenec.mealvity.mealvity.classes.reservations.Reservation
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import cenec.mealvity.mealvity.classes.singleton.RestaurantMoreInfoSingleton
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Order
import cenec.mealvity.mealvity.classes.user.UserDetails
import cenec.mealvity.mealvity.fragments.payment.deliverymethod.DeliveryFragment
import cenec.mealvity.mealvity.fragments.payment.deliverymethod.PickupFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.CardFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.CashFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.PaypalFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import java.util.*

class OrderPaymentActivity : AppCompatActivity() {
    private val REQUEST_CODE_PAYPAL_PAYMENT = 1000
    private val currentUser by lazy { UserSingleton.getInstance().getCurrentUser() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val restaurantName by lazy { RestaurantMoreInfoSingleton.getInstance().getRestaurantMoreInfo()!!.name }
    private lateinit var currentOrder: Order
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var confirmedOrderCart: OrderCart
    private var paymentOption = 0
    private var deliveryOption = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setupNewOrder()
        checkBundleExtras()
        setupToolbar()
        setupViews()
    }

    private fun setupNewOrder() {
        currentOrder = Order()
        OrderSingleton.getInstance().setOrder(currentOrder)
    }

    private fun checkBundleExtras() {
        intent.extras?.let {
            confirmedOrderCart = it.getSerializable("order") as OrderCart
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(confirmedOrderCart)

        binding.cardViewPaymentBill.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding.cardViewPaymentBill.recyclerViewOrderCart.adapter = rvAdapter
    }

    private fun setupDeliveryMethodViewPager() {
        val fAdapter = FragmentAdapter2(this, arrayListOf(
            PickupFragment(),
            DeliveryFragment()
        ))

        binding.cardViewDeliveryOptions.layoutFragment.adapter = fAdapter
        binding.cardViewDeliveryOptions.layoutFragment.isUserInputEnabled = false
    }

    private fun setupPaymentMethodViewPager() {
        val fAdapter = FragmentAdapter2(this, arrayListOf(CashFragment(), CardFragment(), PaypalFragment()))

        binding.cardViewPaymentMethod.layoutFragment.adapter = fAdapter
        binding.cardViewPaymentMethod.layoutFragment.isUserInputEnabled = false
    }

    private fun setupViews() {
        setupToolbar()
        setupRecyclerView()
        setupDeliveryMethodViewPager()
        setupPaymentMethodViewPager()

        setupCardViewPaymentBill()
        setupCardViewUserDetails()
        setupCardViewDeliveryOptions()
        setupCardViewPaymentMethod()
        setupCardViewConfirmationPayment()
    }

    private fun setupCardViewPaymentBill() {
        binding.cardViewPaymentBill.textViewBasePrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrderCart.getBasePrice())
        binding.cardViewPaymentBill.textViewTaxPrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrderCart.getTaxPrice())
        binding.cardViewPaymentBill.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrderCart.totalPrice)
    }

    private fun setupCardViewUserDetails() {
        val currentUser = UserSingleton.getInstance().getCurrentUser()
        binding.cardViewUserDetails.textViewUserName.setText(currentUser.fullName)
        binding.cardViewUserDetails.textViewUserPhone.setText(currentUser.phoneNumber)
        binding.cardViewUserDetails.textViewUserEmail.setText(currentUser.email)
    }

    private fun setupCardViewDeliveryOptions() {
        binding.cardViewDeliveryOptions.textViewPickupOption.setOnClickListener {
            showPickupFragment()
            deliveryOption = 0
        }

        binding.cardViewDeliveryOptions.textViewDeliveryOption.setOnClickListener {
            showDeliveryFragment()
            deliveryOption = 1
        }
    }

    private fun setupCardViewPaymentMethod() {
        binding.cardViewPaymentMethod.textViewCashMethod.setOnClickListener {
            showCashMethodFragment()
            paymentOption = 0
        }

        binding.cardViewPaymentMethod.textViewCardMethod.setOnClickListener {
            showCardMethodFragment()
            paymentOption = 1
        }

        binding.cardViewPaymentMethod.textViewPaypalMethod.setOnClickListener {
            showPaypalMethodFragment()
            paymentOption = 2
        }
    }

    private fun showPickupFragment() {
        binding.cardViewDeliveryOptions.textViewPickupOption.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewDeliveryOptions.textViewPickupOption.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewDeliveryOptions.textViewDeliveryOption.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewDeliveryOptions.textViewDeliveryOption.setTextColor(getColor(R.color.black))

        binding.cardViewDeliveryOptions.layoutFragment.setCurrentItem(0, true)
    }

    private fun showDeliveryFragment() {
        binding.cardViewDeliveryOptions.textViewPickupOption.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewDeliveryOptions.textViewPickupOption.setTextColor(getColor(R.color.black))

        binding.cardViewDeliveryOptions.textViewDeliveryOption.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewDeliveryOptions.textViewDeliveryOption.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewDeliveryOptions.layoutFragment.setCurrentItem(1, true)
    }

    private fun showCashMethodFragment() {
        resetCardViewPaymentButtons()
        binding.cardViewPaymentMethod.textViewCashMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewCashMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(0, true)
    }

    private fun showCardMethodFragment() {
        resetCardViewPaymentButtons()
        binding.cardViewPaymentMethod.textViewCardMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewCardMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(1, true)
    }

    private fun showPaypalMethodFragment() {
        resetCardViewPaymentButtons()
        binding.cardViewPaymentMethod.textViewPaypalMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewPaypalMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(2, true)
    }

    private fun resetCardViewPaymentButtons() {
        binding.cardViewPaymentMethod.textViewPaypalMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewPaypalMethod.setTextColor(getColor(R.color.black))
        binding.cardViewPaymentMethod.textViewCardMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCardMethod.setTextColor(getColor(R.color.black))
        binding.cardViewPaymentMethod.textViewCashMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCashMethod.setTextColor(getColor(R.color.black))
    }

    private fun setupCardViewConfirmationPayment() {
        binding.cardViewPaymentConfirmation.cardViewPaymentConfirmation.setOnClickListener {
            when (paymentOption) {
                0, 1 -> {
                    createNewOrder()
                    addOrderToRestaurantDatabase()
                }
                2 -> {
                    startPaypalActivity()
                }
            }
        }
    }

    private fun createNewOrder() {
        val etFullName = binding.cardViewUserDetails.textViewUserName.text.toString()
        val etPhoneNumber = binding.cardViewUserDetails.textViewUserPhone.text.toString()
        val etEmail = binding.cardViewUserDetails.textViewUserEmail.text.toString()

        when {
            etFullName.isEmpty() -> {
                binding.cardViewUserDetails.textViewUserName.requestFocus()
                binding.cardViewUserDetails.textViewUserName.error = getString(R.string.text_field_empty)
            }
            etPhoneNumber.isEmpty() -> {
                binding.cardViewUserDetails.textViewUserPhone.requestFocus()
                binding.cardViewUserDetails.textViewUserPhone.error = getString(R.string.text_field_empty)
            }
            etEmail.isEmpty() -> {
                binding.cardViewUserDetails.textViewUserEmail.requestFocus()
                binding.cardViewUserDetails.textViewUserEmail.error = getString(R.string.text_field_empty)
            }
            else -> {
                var method: Order.PaymentMethod? = null
                val userDetails = UserDetails(currentUser.userId, etFullName, etPhoneNumber, etEmail)
                when (paymentOption) {
                    0 -> method = Order.PaymentMethod.CASH
                    1 -> method = Order.PaymentMethod.CARD
                    2 -> method = Order.PaymentMethod.PAYPAL
                }

                if (deliveryOption == 1) {
                    if (currentOrder.addressSelected.isEmpty()) {
                        Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show()
                    } else {
                        currentOrder.deliveryMode = Order.DeliveryMode.DELIVERY

                        currentOrder.orderCart = confirmedOrderCart
                        currentOrder.paymentMethod = method
                        currentOrder.user = userDetails
                        currentOrder.restaurantName = restaurantName

                        currentUser.addOrder(currentOrder)
                    }
                } else if (deliveryOption == 0) {
                    currentOrder.addressSelected = ""
                    currentOrder.deliveryMode = Order.DeliveryMode.PICKUP

                    currentOrder.orderCart = confirmedOrderCart
                    currentOrder.paymentMethod = method
                    currentOrder.user = userDetails
                    currentOrder.restaurantName = restaurantName

                    currentUser.addOrder(currentOrder)
                }
            }
        }
    }

    private fun updateUserInDatabase() {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentFirebaseUser!!.uid)
            .set(currentUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Order confirmed!", Toast.LENGTH_LONG).show()
                    val intentHomePage = Intent(this, FragmentContainerActivity::class.java)
                    intentHomePage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentHomePage)
                } else {
                    Toast.makeText(this, "Error confirming order", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun addOrderToRestaurantDatabase() {
        val restaurantNameString = restaurantName.replace(" ", "").toLowerCase(Locale.ROOT)

        mFirebaseFirestore.collection("restaurants").document(restaurantNameString)
            .update("orders", FieldValue.arrayUnion(currentOrder))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInDatabase()
                } else {
                    createRestaurantDatabase(restaurantNameString)
                    println(task.exception)
                }
            }
    }

    private fun createRestaurantDatabase(restaurantName: String) {
        mFirebaseFirestore.collection("restaurants").document(restaurantName)
            .set(hashMapOf(
                "name" to restaurantName,
                "reservations" to arrayListOf<Reservation>(),
                "orders" to arrayListOf<Order>()
            ))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addOrderToRestaurantDatabase()
                } else {
                    Toast.makeText(this, "Error creating database", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun startPaypalActivity() {
        val paypalPayment = PaypalConfig.generatePaypalPayment(confirmedOrderCart.totalPrice+0.50f)
        val paypalConfiguration = PaypalConfig.createPaypalConfiguration()

        val intentPaypal = Intent(this, PaymentActivity::class.java)
        intentPaypal.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfiguration)
        intentPaypal.putExtra(PaymentActivity.EXTRA_PAYMENT, paypalPayment)

        startActivityForResult(intentPaypal, REQUEST_CODE_PAYPAL_PAYMENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PAYPAL_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    createNewOrder()
                    addOrderToRestaurantDatabase()
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment with Paypal canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
