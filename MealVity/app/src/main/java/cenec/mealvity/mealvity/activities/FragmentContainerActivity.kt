package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter
import cenec.mealvity.mealvity.fragments.main.HomeTabFragment
import cenec.mealvity.mealvity.fragments.main.OrdersTabFragment
import cenec.mealvity.mealvity.fragments.main.ProfileTabFragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener

/**
 * Activity containing the main fragments of the application (Home, Orders, Profile)
 */
class FragmentContainerActivity : AppCompatActivity() {
    private val vpFragment by lazy { findViewById<ViewPager>(R.id.view_pager) } // ViewPager of the fragment
    private val navigation by lazy { findViewById<BubbleNavigationLinearView>(R.id.navigation_menu) } // Navigation menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        setupViewPager()
        // Every time we change the fragment, we set the navigation to it's correct position
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

        navigation.setNavigationChangeListener(object: BubbleNavigationChangeListener{
            override fun onNavigationChanged(view: View?, position: Int) {
                vpFragment.setCurrentItem(position, true)
            }

        })
    }

    /**
     * Function used to setup the viewPager for the Fragments
     */
    private fun setupViewPager() {
        val fragmentAdapter=FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(HomeTabFragment())
        fragmentAdapter.addFragment(OrdersTabFragment())
        fragmentAdapter.addFragment(ProfileTabFragment())

        vpFragment.adapter=fragmentAdapter
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
