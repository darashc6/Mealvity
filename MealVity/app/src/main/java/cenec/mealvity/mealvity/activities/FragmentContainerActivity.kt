package cenec.mealvity.mealvity.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.fragments.HomeTabFragment
import cenec.mealvity.mealvity.fragments.OrdersTabFragment
import cenec.mealvity.mealvity.fragments.ProfileTabFragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView

class FragmentContainerActivity : AppCompatActivity() {
    private val vpFragment by lazy { findViewById<ViewPager>(R.id.view_pager) }
    private val navigation by lazy { findViewById<BubbleNavigationLinearView>(R.id.navigation_menu) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        setupViewPager()
        vpFragment.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                navigation.setCurrentActiveItem(position)
            }

        })

        navigation.setNavigationChangeListener { _, position ->
            vpFragment.setCurrentItem(position, true)
        }
    }

    private fun setupViewPager() {
        val fragmentAdapter=FragmentAdapter(supportFragmentManager)
        val homeTab=HomeTabFragment()
        val ordersTab=OrdersTabFragment()
        val profileTab=ProfileTabFragment()
        fragmentAdapter.addFragment(homeTab)
        fragmentAdapter.addFragment(ordersTab)
        fragmentAdapter.addFragment(profileTab)

        vpFragment.adapter=fragmentAdapter
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
