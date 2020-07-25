package cenec.mealvity.mealvity.classes.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.BottomSheetFilterListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import java.lang.ClassCastException

/**
 * Bottom Sheet used for applyting filters to the RestaurantList
 * @param appContext Application context
 * @param mapCustomParameters HashMap of custom parameters, used when querying to the API
 */
class FilterListBottomSheet(private var appContext: Context, private var mapCustomParameters: HashMap<String, String>) : BottomSheetDialogFragment() {

    /**
     * Parameter names
     */
    companion object {
        private const val PARAMETER_PRICE_RANGES = "price"
        private const val PARAMETER_RADIUS_LIMIT = "radius"
        private const val PARAMETER_OPEN_RESTAURANTS = "openNow"
    }

    private var _binding: BottomSheetFilterListBinding? = null // View binding for the layout used for this Bottom Sheet
    private val binding get() = _binding!! // Non-nullable version the view binding variable above
    private lateinit var bsListener: FilterListBottomSheetListener // Listener for the Bottom Sheet
    private var rangePriceSelected = arrayListOf<Int>() // List of price ranges selected
    private var radiusLimit = 1000 // Radius limit of the search
    private var showOpenRestaurants = false // True if user wants to see only those restaurants that are open, false if otherwise

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
     * Applies the custom parameters to the view
     * Checks the Checkboxes necessary, puts the seekbar in the correct position
     */
    private fun applyCustomParametersToView() {
        if (mapCustomParameters.containsKey(PARAMETER_PRICE_RANGES)) {
            val listPriceRanges = mapCustomParameters[PARAMETER_PRICE_RANGES]!!.split(",")
            for (price in listPriceRanges) {
                when (price) {
                    "1" -> binding.checkBoxPrice1.isChecked = true
                    "2" -> binding.checkBoxPrice2.isChecked = true
                    "3" -> binding.checkBoxPrice3.isChecked = true
                    "4" -> binding.checkBoxPrice4.isChecked = true
                }
            }
        }

        if (mapCustomParameters.containsKey(PARAMETER_RADIUS_LIMIT)) {
            radiusLimit = Integer.parseInt(mapCustomParameters[PARAMETER_RADIUS_LIMIT]!!)
            when (radiusLimit) {
                1000 -> binding.distanceSeekbar.setProgress(0f)
                2000 -> binding.distanceSeekbar.setProgress(25f)
                5000 -> binding.distanceSeekbar.setProgress(50f)
                8000 -> binding.distanceSeekbar.setProgress(75f)
                10000 -> binding.distanceSeekbar.setProgress(100f)
            }
        }

        if (mapCustomParameters.containsKey(PARAMETER_OPEN_RESTAURANTS)) {
            binding.checkBoxRestaurantsOpen.isChecked = true
        }
    }

    /**
     * Sets up the views in the Bottom Sheet
     */
    private fun setupViews() {
        if (mapCustomParameters.isNotEmpty()) {
            applyCustomParametersToView()
        }

        binding.checkBoxPrice1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) rangePriceSelected.add(1) else rangePriceSelected.remove(1)
        }

        binding.checkBoxPrice2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) rangePriceSelected.add(2) else rangePriceSelected.remove(2)
        }

        binding.checkBoxPrice3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) rangePriceSelected.add(3) else rangePriceSelected.remove(3)
        }

        binding.checkBoxPrice4.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) rangePriceSelected.add(4) else rangePriceSelected.remove(4)
        }

        binding.distanceSeekbar.customTickTexts(appContext.resources.getStringArray(R.array.array_distances))
        binding.distanceSeekbar.onSeekChangeListener = object : OnSeekChangeListener {
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

        binding.checkBoxRestaurantsOpen.setOnCheckedChangeListener { _, isChecked ->
            showOpenRestaurants = isChecked
        }

        binding.buttonApplyFilter.setOnClickListener {
            bsListener.onApplyFiltersClick(getCustomParamenters())
            dismiss()
        }

        binding.buttonClearFilters.setOnClickListener {
            bsListener.onClearFiltersClick()
            dismiss()
        }
    }

    /**
     * Returns a HashMap of the parameters applied in the views
     * @return HashMap with parameters
     */
    private fun getCustomParamenters(): HashMap<String, String> {
        val paramsMap = hashMapOf<String, String>()

        if (rangePriceSelected.isNotEmpty()) paramsMap[PARAMETER_PRICE_RANGES] = priceRangeListToString()
        paramsMap[PARAMETER_RADIUS_LIMIT] = radiusLimit.toString()
        if (showOpenRestaurants) paramsMap[PARAMETER_OPEN_RESTAURANTS] = showOpenRestaurants.toString()

        return paramsMap
    }

    /**
     * Converts the prices selected from the checkbox to String, for querying purposes
     */
    private fun priceRangeListToString(): String {
        var priceRanges = ""

        for (price in rangePriceSelected) {
            priceRanges += "${price},"
        }

        return priceRanges.substring(0, priceRanges.length - 1)
    }

    /**
     * Interface for the Bottom Sheet
     */
    interface FilterListBottomSheetListener {
        /**
         * Applies the filters to the search query
         * @param customParameters HashMap with parameters for the filter
         */
        fun onApplyFiltersClick(customParameters: HashMap<String, String>)

        /**
         * Clears the filters previously applied to the search query
         */
        fun onClearFiltersClick()
    }
}