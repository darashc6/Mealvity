package cenec.mealvity.mealvity.classes.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Adapter for containing the fragments
 * @param fragmentManager Activity's fragment manager
 */
class FragmentAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var listFragments=arrayListOf<Fragment>() // List of fragments

    override fun getItem(position: Int): Fragment {
        return listFragments[position]
    }

    override fun getCount(): Int {
        return listFragments.size
    }

    fun addFragment (frag: Fragment) {
        listFragments.add(frag)
    }
}