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

/**
 * Bottom sheet used for adding a menu item to the order cart
 * @param menuItem menu item to add to cart
 */
class AddItemToCartBottomSheet(private var menuItem: Item): BottomSheetDialogFragment() {
    private var _binding: BottomSheetAddItemToCartBinding? = null // View binding of the Bottom Sheet
    private val binding get() = _binding!! // Non-nullable version of the binding variable above
    private var itemQuantity = 1 // Item quantity, can't be less than 1
    private lateinit var bsListener: AddItemToCartBottomSheetListener // Listener for the bottom sheet


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetAddItemToCartBinding.inflate(inflater, container, false)
        expandBottomSheetOnShow(dialog!!)
        setupViews()
        return binding.root
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

    /**
     * Sets up the views of the bottom sheet
     */
    private fun setupViews() {
        binding.textViewItemName.text = menuItem.name
        binding.textViewItemDescription.text = menuItem.description
        binding.textViewItemPrice.text = String.format(Locale.getDefault(), "€%.2f", menuItem.price)
        binding.textViewQuantity.text = itemQuantity.toString()
        updateTotalPrice()

        binding.buttonAddQuantity.setOnClickListener {
            addQuantity()
        }

        binding.buttonRemoveQuantity.setOnClickListener {
            removeQuantity()
        }

        binding.buttonAddItemToCart.setOnClickListener {
            bsListener.onAddItemToCart(menuItem, itemQuantity)
            dismiss()
        }
    }

    /**
     * Adds a quantity of the item
     */
    private fun addQuantity() {
        itemQuantity++
        binding.textViewQuantity.text = itemQuantity.toString()
        updateTotalPrice()
    }

    /**
     * Retracts a quantity of the item
     */
    private fun removeQuantity() {
        if (itemQuantity > 1) {
            itemQuantity--
            binding.textViewQuantity.text = itemQuantity.toString()
            updateTotalPrice()
        }
    }

    /**
     * Updates the price total whenever the item quantity is modified
     */
    private fun updateTotalPrice() {
        val totalPrice = menuItem.price * itemQuantity
        binding.textViewTotalPrice.text = String.format(Locale.getDefault(), "€%.2f", totalPrice)
    }

    /**
     * Sets the listener of the bottom sheet
     * @param listener New listener
     */
    fun setAddItemToBottomSheetListener(listener: AddItemToCartBottomSheetListener) {
        bsListener = listener
    }

    /**
     * Interface for thw bottom sheet
     */
    interface AddItemToCartBottomSheetListener {
        /**
         * Triggered whenever the user clicks the 'Add to cart' button
         */
        fun onAddItemToCart(menuItem: Item, quantity: Int)
    }
}