package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.ActivityPaymentBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter2
import cenec.mealvity.mealvity.classes.orders.Order
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.fragments.payment.deliverymethod.DeliveryFragment
import cenec.mealvity.mealvity.fragments.payment.deliverymethod.PickupFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.CardFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.CashFragment
import cenec.mealvity.mealvity.fragments.payment.paymentmethod.PaypalFragment
import java.util.*

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var confirmedOrder: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        checkBundleExtras()
        setupToolbar()
        setupViews()
    }

    private fun checkBundleExtras() {
        intent.extras?.let {
            confirmedOrder = it.getSerializable("order") as Order
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(confirmedOrder)

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
    }

    private fun setupCardViewPaymentBill() {
        binding.cardViewPaymentBill.textViewBasePrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrder.getBasePrice())
        binding.cardViewPaymentBill.textViewTaxPrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrder.getTaxPrice())
        binding.cardViewPaymentBill.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", confirmedOrder.totalPrice)
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
        }

        binding.cardViewDeliveryOptions.textViewDeliveryOption.setOnClickListener {
            showDeliveryFragment()
        }
    }

    private fun setupCardViewPaymentMethod() {
        binding.cardViewPaymentMethod.textViewCashMethod.setOnClickListener {
            showCashMethodFragment()
        }

        binding.cardViewPaymentMethod.textViewCardMethod.setOnClickListener {
            showCardMethodFragment()
        }

        binding.cardViewPaymentMethod.textViewPaypalMethod.setOnClickListener {
            showPaypalMethodFragment()
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
        binding.cardViewPaymentMethod.textViewCashMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewCashMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.textViewCardMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCardMethod.setTextColor(getColor(R.color.black))
        binding.cardViewPaymentMethod.textViewPaypalMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewPaypalMethod.setTextColor(getColor(R.color.black))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(0, true)
    }

    private fun showCardMethodFragment() {
        binding.cardViewPaymentMethod.textViewCardMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewCardMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.textViewCashMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCashMethod.setTextColor(getColor(R.color.black))
        binding.cardViewPaymentMethod.textViewPaypalMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewPaypalMethod.setTextColor(getColor(R.color.black))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(1, true)
    }

    private fun showPaypalMethodFragment() {
        binding.cardViewPaymentMethod.textViewPaypalMethod.background = getDrawable(R.drawable.custom_border_with_color_primary)
        binding.cardViewPaymentMethod.textViewPaypalMethod.setTextColor(getColor(R.color.colorAccent))

        binding.cardViewPaymentMethod.textViewCardMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCardMethod.setTextColor(getColor(R.color.black))
        binding.cardViewPaymentMethod.textViewCashMethod.background = getDrawable(R.drawable.custom_border_with_color_accent_dark)
        binding.cardViewPaymentMethod.textViewCashMethod.setTextColor(getColor(R.color.black))

        binding.cardViewPaymentMethod.layoutFragment.setCurrentItem(2, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}
