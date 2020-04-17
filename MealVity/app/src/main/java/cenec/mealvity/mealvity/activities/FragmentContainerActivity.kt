package cenec.mealvity.mealvity.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.fragment.FragmentAdapter
import cenec.mealvity.mealvity.classes.models.UserModel
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.classes.viewmodels.UserViewModel
import cenec.mealvity.mealvity.fragments.main.HomeTabFragment
import cenec.mealvity.mealvity.fragments.main.OrdersTabFragment
import cenec.mealvity.mealvity.fragments.main.ProfileTabFragment
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener

class FragmentContainerActivity : AppCompatActivity() {
    private val vpFragment by lazy { findViewById<ViewPager>(R.id.view_pager) }
    private val navigation by lazy { findViewById<BubbleNavigationLinearView>(R.id.navigation_menu) }
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        userViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.setUserLiveData(UserModel.getInstance().getCurrentUser())

        UserModel.getInstance().setUserModelListener(object : UserModel.UserModelListener{
            override fun onUserUpdate(updatedUser: User) {
                userViewModel.setUserLiveData(updatedUser)
            }

        })

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

        navigation.setNavigationChangeListener(object: BubbleNavigationChangeListener{
            override fun onNavigationChanged(view: View?, position: Int) {
                vpFragment.setCurrentItem(position, true)
            }

        })
    }

    private fun setupViewPager() {
        val fragmentAdapter=FragmentAdapter(supportFragmentManager)
        val homeTab= HomeTabFragment()
        val ordersTab= OrdersTabFragment()
        val profileTab= ProfileTabFragment()
        fragmentAdapter.addFragment(homeTab)
        fragmentAdapter.addFragment(ordersTab)
        fragmentAdapter.addFragment(profileTab)

        vpFragment.adapter=fragmentAdapter
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    fun getUserViewModel(): UserViewModel {
        return userViewModel
    }
}
