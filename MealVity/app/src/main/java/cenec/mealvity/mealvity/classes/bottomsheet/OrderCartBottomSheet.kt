package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.databinding.BottomSheetOrderCartBinding
import cenec.mealvity.mealvity.classes.adapters.OrderCartRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.orders.Order
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class OrderCartBottomSheet(private var appContext: Context, private var order: Order): BottomSheetDialogFragment() {
    private var _binding: BottomSheetOrderCartBinding? = null
    private val binding get() = _binding
    private lateinit var bsListener: OrderCartBottomSheetListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetOrderCartBinding.inflate(inflater, container, false)
        expandBottomSheetOnShow(dialog!!)
        setupViews()
        return binding!!.root
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

    private fun setupViews() {
        binding!!.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", order.totalPrice)
        if (order.orderCart.isEmpty()) {
            binding!!.recyclerViewOrderCart.visibility = View.GONE
            binding!!.textViewEmptyCart.visibility = View.VISIBLE
        } else {
            setupRecyclerView()
            binding!!.recyclerViewOrderCart.visibility = View.VISIBLE
            binding!!.textViewEmptyCart.visibility = View.GONE
        }

        binding!!.buttonConfirmOrder.setOnClickListener {
            bsListener.onConfirmOrderClick()
            dismiss()
        }
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(appContext, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = OrderCartRecyclerViewAdapter(order)

        binding!!.recyclerViewOrderCart.layoutManager = rvLayoutManager
        binding!!.recyclerViewOrderCart.adapter = rvAdapter
    }

    fun setOrderCartBottomSheetListener(listener: OrderCartBottomSheetListener) {
        bsListener = listener
    }

    interface OrderCartBottomSheetListener {
        fun onConfirmOrderClick()
    }
}