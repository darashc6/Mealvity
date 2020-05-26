package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.databinding.BottomSheetAddItemToCartBinding
import cenec.mealvity.mealvity.classes.restaurant.menu.Item
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException
import java.util.*

class AddItemToCartBottomSheet(private var menuItem: Item): BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddItemToCartBinding? = null
    private val binding get() = _binding
    private var itemQuantity = 1
    private lateinit var bsListener: AddItemToCartBottomSheetListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetAddItemToCartBinding.inflate(inflater, container, false)
        expandBottomSheetOnShow(dialog!!)
        setupViews()
        return binding!!.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
        binding!!.textViewItemName.text = menuItem.name
        binding!!.textViewItemDescription.text = menuItem.description
        binding!!.textViewItemPrice.text = String.format(Locale.getDefault(), "€%.2f", menuItem.price)
        binding!!.textViewQuantity.text = itemQuantity.toString()
        updateTotalPrice()

        binding!!.buttonAddQuantity.setOnClickListener {
            addQuantity()
        }

        binding!!.buttonRemoveQuantity.setOnClickListener {
            removeQuantity()
        }

        binding!!.buttonAddItemToCart.setOnClickListener {
            bsListener.onAddItemToCart(menuItem, itemQuantity)
            dismiss()
        }
    }

    private fun addQuantity() {
        itemQuantity++
        binding!!.textViewQuantity.text = itemQuantity.toString()
        updateTotalPrice()
    }

    private fun removeQuantity() {
        if (itemQuantity > 1) itemQuantity-- else itemQuantity = 1
        binding!!.textViewQuantity.text = itemQuantity.toString()
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = menuItem.price * itemQuantity
        binding!!.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", totalPrice)
    }

    fun setAddItemToBottomSheetListener(listener: AddItemToCartBottomSheetListener) {
        bsListener = listener
    }

    interface AddItemToCartBottomSheetListener {
        fun onAddItemToCart(menuItem: Item, quantity: Int)
    }
}