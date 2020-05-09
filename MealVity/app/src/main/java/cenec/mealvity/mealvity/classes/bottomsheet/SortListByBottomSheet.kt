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

class SortListByBottomSheet(private var appContext: Context, private var sortListOptSelected: Int): BottomSheetDialogFragment() {
    private var _binding: BottomSheetSortListByBinding? = null
    private val binding get() = _binding
    private lateinit var bsListener: SortListByBottomSheetListener

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

    private fun expandBottomSheetOnShow(dialog: Dialog) {
        dialog.setOnShowListener {
            val dialogBottomSheet = it as BottomSheetDialog
            val internalDialog = dialogBottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(internalDialog as View).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupViews() {
        when (sortListOptSelected) {
            0 -> binding!!.cardViewSortBestMatch.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            1 -> binding!!.cardViewSortRating.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            2 -> binding!!.cardViewSortDistance.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            3 -> binding!!.cardViewSortEconomicPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
            4 -> binding!!.cardViewSortLuxuriousPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDarkDark))
        }

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
            bsListener.onSortListClick(sortListOptSelected)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun resetCardViews() {
        when (sortListOptSelected) {
            0 -> binding!!.cardViewSortBestMatch.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            1 -> binding!!.cardViewSortRating.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            2 -> binding!!.cardViewSortDistance.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            3 -> binding!!.cardViewSortEconomicPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
            4 -> binding!!.cardViewSortLuxuriousPrice.setCardBackgroundColor(appContext.getColor(R.color.colorAccentDark))
        }
    }

    interface SortListByBottomSheetListener {
        fun onSortListClick(newOptionSelected: Int)
    }

}