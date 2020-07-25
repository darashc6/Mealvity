package cenec.mealvity.mealvityforowners.features.orderlist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.mealvity.mealvityforowners.FragmentContainerActivity
import cenec.mealvity.mealvityforowners.OrderInfoActivity

import cenec.mealvity.mealvityforowners.RestaurantDatabaseSingleton
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase
import cenec.mealvity.mealvityforowners.core.order.adapter.OrderSingleton
import cenec.mealvity.mealvityforowners.databinding.FragmentOrderListBinding
import cenec.mealvity.mealvityforowners.features.orderlist.adapter.OrderListRecyclerViewAdapter

/**
 * Fragment showing a list of orders received by the restaurant
 */
class OrderListFragment : Fragment(), FragmentContainerActivity.FragmentContainerActivityListener {
    private var rvAdapter: OrderListRecyclerViewAdapter? = null // RecyclerView adapter
    private var dbRestaurant = RestaurantDatabaseSingleton.getInstance().getRestaurantDatabase() // Database of the restaurant
    private var filterOpt = 0 // Option for the filter (0 - All orders, 1 - Pending, 2 - Accepted, 3 - Rejected)
    private val viewModel by lazy { (context as FragmentContainerActivity).getViewModel() } // ViewModel of RestaurantDatabase
    private var _binding: FragmentOrderListBinding? = null // View binding for the fragment
    private val binding get() = _binding!! // Non-nullable version of teh binding variable above

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderListBinding.inflate(layoutInflater)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRestaurantDatabase().observe(viewLifecycleOwner, Observer { newRestaurantDatabase ->
            dbRestaurant = newRestaurantDatabase
            filterOrdersList()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets up a RecyclerView containing a list of orders
     */
    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvAdapter = OrderListRecyclerViewAdapter(dbRestaurant.orders)
        rvAdapter!!.setOrderListRecyclerViewListener(object : OrderListRecyclerViewAdapter.OrderListRecyclerViewListener{
            override fun onItemClick(position: Int) {
                OrderSingleton.getInstance().setOrder(dbRestaurant.orders[position])
                val intentOrderInfo = Intent(context, OrderInfoActivity::class.java)
                startActivity(intentOrderInfo)
            }

        })

        binding.orderListRecyclerView.layoutManager = rvLayoutManager
        binding.orderListRecyclerView.adapter = rvAdapter
    }

    /**
     * Filters the order list depending on the option selected
     */
    private fun filterOrdersList() {
        when (filterOpt) {
            0 -> rvAdapter?.setOrderList(dbRestaurant.showAllOrders())
            1 -> rvAdapter?.setOrderList(dbRestaurant.showPendingOrders())
            2 -> rvAdapter?.setOrderList(dbRestaurant.showAcceptedOrders())
            3 -> rvAdapter?.setOrderList(dbRestaurant.showRejectedOrders())
        }
    }

    override fun onFilterOptionSelected(filterOptSelected: Int) {
        filterOpt = filterOptSelected
        filterOrdersList()
    }

}
