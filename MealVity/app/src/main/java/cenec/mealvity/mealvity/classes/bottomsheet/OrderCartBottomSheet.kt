package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.databinding.BottomSheetOrderCartBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.orders.OrderCart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

/**
 * Bottom Sheet used for displaying the order cart
 * @param appContext Application context
 * @param orderCart Order cart
 */
class OrderCartBottomSheet(private var appContext: Context, private var orderCart: OrderCart): BottomSheetDialogFragment() {
    private var _binding: BottomSheetOrderCartBinding? = null // View binding of the bottom sheet
    private val binding get() = _binding!! // Non-nullable version of the binding variable above
    private lateinit var bsListener: OrderCartBottomSheetListener // Listener for the bottom sheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetOrderCartBinding.inflate(inflater, container, false)
        expandBottomSheetOnShow(dialog!!)
        setupViews()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Shows an expanded version of the Bottom Sheet
     * @param dialog The BottomSheetDialog shown
     */
    private fun expandBottomSheetOnShow(dialog: Dialog) {
        dialog.setOnShowListener {
            val dialogBottomSheet = it as BottomSheetDialog
            val internalDialog =
                dialogBottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(internalDialog as View).state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }

    /**
     * Sets up the views of the bottom sheet
     */
    private fun setupViews() {
        binding.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", orderCart.totalPrice)
        if (orderCart.orderCart.isEmpty()) {
            binding.recyclerViewOrderCart.visibility = View.GONE
            binding.textViewEmptyCart.visibility = View.VISIBLE
        } else {
            setupRecyclerView()
            binding.recyclerViewOrderCart.visibility = View.VISIBLE
            binding.textViewEmptyCart.visibility = View.GONE
        }

        binding.buttonConfirmOrder.setOnClickListener {
            bsListener.onConfirmOrderClick()
            dismiss()
        }
    }

    /**
     * Sets up the RecyclerView containing the Order's cart
     */
    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(orderCart)

        binding.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding.recyclerViewOrderCart.adapter = rvAdapter
    }

    /**
     * Sets the listener of the bottom sheet
     * @param listener New listener
     */
    fun setOrderCartBottomSheetListener(listener: OrderCartBottomSheetListener) {
        bsListener = listener
    }

    /**
     * Inteface for the bottom sheet
     */
    interface OrderCartBottomSheetListener {
        /**
         * Triggered when the user clicks the 'Confirm Order' button
         */
        fun onConfirmOrderClick()
    }
}