package cenec.mealvity.mealvity.fragments.restaurantmoreinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cenec.darash.mealvity.databinding.FragmentBookTableBinding
import cenec.mealvity.mealvity.activities.RestaurantMoreInfoActivity
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo


class FragmentBookTable : Fragment(), RestaurantMoreInfoActivity.RestaurantMoreInfoListener {
    private var _binding: FragmentBookTableBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookTableBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onInfoLoaded(example: RestaurantMoreInfo) {
        binding!!.loadingProgressBar.visibility = View.GONE
        binding!!.bookTableLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
