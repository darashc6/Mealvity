package cenec.mealvity.mealvityforowners.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Adapter for containing the fragments
 * @param fragmentManager Activity's fragment manager
 * @param listFragment List of fragments
 */
class ViewPagerAdapter(fragmentManager: FragmentManager, var listFragment: ArrayList<Fragment>):
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }
}