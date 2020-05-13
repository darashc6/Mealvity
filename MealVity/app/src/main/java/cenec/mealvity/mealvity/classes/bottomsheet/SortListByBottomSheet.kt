package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.BottomSheetSortListByBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.ClassCastException

/**
 * Bottom Sheet used for sorting the RestaurantList
 * @param appContext Application context
 * @param sortListOptSelected Option selected for sorting the list (0 - Best match, 1 - Ratings, 2 - Distance, 3 - Price (€ - €€€€), 4 - Price (€€€€ - €))
 */
class SortListByBottomSheet(private var appContext: Context, private var sortListOptSelected: Int): BottomSheetDialogFragment() {
    private var _binding: BottomSheetSortListByBinding? = null // View binding for the layout used for this Bottom Sheet
    private val binding get() = _binding // Non-nullable version of the view binding variable above
    private lateinit var bsListener: SortListByBottomSheetListener // Listener for the Bottom Sheet

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            bsListener = context as SortListByBottomSheetListener
        } catch (ex: ClassCastException) {
            throw ClassCastException("$context must implement SortListByBottomSheetListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetSortListByBinding.inflate(inflater, container, false)
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
            val internalDialog = dialogBottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(internalDialog as View).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    /**
     * Sets the background color of the CardView when the Bottom Sheet is first shown, depending on the option selected
     */
    private fun initialOptionSelected() {
        when (sortListOptSelected) {
            0 -> binding!!.cardViewSortBestMatch.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            1 -> binding!!.cardViewSortRating.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            2 -> binding!!.cardViewSortDistance.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            3 -> binding!!.cardViewSortEconomicPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            4 -> binding!!.cardViewSortLuxuriousPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
        }
    }

    /**
     * Sets up the views for this Bottom Sheet
     */
    private fun setupViews() {
        initialOptionSelected()

        binding!!.cardViewSortBestMatch.setOnClickListener {
            resetCardViews()
            binding!!.cardViewSortBestMatch.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            sortListOptSelected = 0
        }

        binding!!.cardViewSortRating.setOnClickListener {
            resetCardViews()
            binding!!.cardViewSortRating.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            sortListOptSelected = 1
        }

        binding!!.cardViewSortDistance.setOnClickListener {
            resetCardViews()
            binding!!.cardViewSortDistance.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            sortListOptSelected = 2
        }

        binding!!.cardViewSortEconomicPrice.setOnClickListener {
            resetCardViews()
            binding!!.cardViewSortEconomicPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            sortListOptSelected = 3
        }

        binding!!.cardViewSortLuxuriousPrice.setOnClickListener {
            resetCardViews()
            binding!!.cardViewSortLuxuriousPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            sortListOptSelected = 4
        }

        binding!!.buttonSortList.setOnClickListener {
            bsListener.onSortList(sortListOptSelected)
            dismiss()
        }
    }

    /**
     * Resets the background color of the CardView, depending on the option selected
     */
    private fun resetCardViews() {
        when (sortListOptSelected) {
            0 -> binding!!.cardViewSortBestMatch.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            1 -> binding!!.cardViewSortRating.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            2 -> binding!!.cardViewSortDistance.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            3 -> binding!!.cardViewSortEconomicPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            4 -> binding!!.cardViewSortLuxuriousPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
        }
    }

    /**
     * Interface for the Bottom Sheet
     */
    interface SortListByBottomSheetListener {
        /**
         * Sorts the RestaurantList depending on the option selected
         * @param newOptionSelected New option selected
         */
        fun onSortList(newOptionSelected: Int)
    }

}