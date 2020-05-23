package cenec.mealvity.mealvity.classes.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter2(activity: AppCompatActivity, private val listFragment: List<Fragment>): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

}