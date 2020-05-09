package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.BottomSheetFilterListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.lang.ClassCastException

class FilterListBottomSheet(private var appContext: Context): BottomSheetDialogFragment() {
    private var _binding: BottomSheetFilterListBinding? = null
    private val binding get() = _binding
    private lateinit var bsListener: FilterListBottomSheetListener
    private var rangePriceSelected = arrayListOf<Int>()
    private var radiusLimit = 1000
    private var showOpenRestaurants = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            bsListener = context as FilterListBottomSheetListener
        } catch (ex: ClassCastException) {
            throw ClassCastException("$context must implement FilterListBottomSheetListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetFilterListBinding.inflate(inflater, container, false)
        expandBottomSheetOnShow(dialog!!)
        setupViews()
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun expandBottomSheetOnShow(dialog: Dialog) {
        dialog.setOnShowListener {
            val dialogBottomSheet = it as BottomSheetDialog
            val internalDialog = dialogBottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(internalDialog as View).state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupViews() {
        binding!!.checkBoxPrice1.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) rangePriceSelected.add(1) else rangePriceSelected.remove(1)
            }
        })

        binding!!.checkBoxPrice2.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) rangePriceSelected.add(2) else rangePriceSelected.remove(2)
            }
        })

        binding!!.checkBoxPrice3.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) rangePriceSelected.add(3) else rangePriceSelected.remove(3)
            }
        })

        binding!!.checkBoxPrice4.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) rangePriceSelected.add(4) else rangePriceSelected.remove(4)
            }
        })

        binding!!.distanceSeekbar.customTickTexts(appContext.resources.getStringArray(R.array.array_distances))
        binding!!.distanceSeekbar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                when (seekParams!!.thumbPosition) {
                    0 -> radiusLimit = 1000
                    1 -> radiusLimit = 2000
                    2 -> radiusLimit = 5000
                    3 -> radiusLimit = 8000
                    4 -> radiusLimit = 10000
                }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

        }

        binding!!.checkBoxRestaurantsOpen.setOnCheckedChangeListener { _, isChecked ->
            showOpenRestaurants = isChecked
        }

        binding!!.buttonApplyFilter.setOnClickListener {
            bsListener.onApplyFiltersClick(getCustomParamenters())
            dismiss()
        }
    }

    private fun getCustomParamenters(): HashMap<String, String> {
        val paramsMap = hashMapOf<String, String>()

        if (rangePriceSelected.isNotEmpty()) paramsMap["price"] = priceRangeListToString()
        paramsMap["radius"] = radiusLimit.toString()
        if (showOpenRestaurants) paramsMap["openNow"] = showOpenRestaurants.toString()

        return paramsMap
    }

    private fun priceRangeListToString(): String {
        var priceRanges = ""

        for (price in rangePriceSelected) {
            priceRanges += "${price},"
        }

        return priceRanges.substring(0, priceRanges.length-1)
    }

    interface FilterListBottomSheetListener {
        fun onApplyFiltersClick(customParameters: HashMap<String, String>)
    }
}