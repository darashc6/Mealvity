package cenec.mealvity.mealvity.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.databinding.FragmentOrdersTabBinding
import cenec.mealvity.mealvity.activities.OrderInfoActivity
import cenec.mealvity.mealvity.classes.adapters.OrderRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Order
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment of the Orders Tab
 */
class OrdersTabFragment : Fragment() {
    private val currentUser by lazy { UserSingleton.getInstance().getCurrentUser() } // User currently logged in
    private var rvAdapter: OrderRecyclerViewAdapter? = null // RecyclerView adapter
    private var _binding: FragmentOrdersTabBinding? = null // View binding for the fragment
    private val binding get() = _binding!! // Non-nullable version of the binding variable above

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersTabBinding.inflate(inflater, container, false)
        setupRecyclerView()
        listenForDatabaseChanges()
        return binding.root
    }

    /**
     * Sets up the RecyclerView, containing a list of orders
     */
    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvAdapter = OrderRecyclerViewAdapter(currentUser.showAllOrders())
        rvAdapter!!.setOrderRecyclerViewListener(object: OrderRecyclerViewAdapter.OrderRecyclerViewListener{
            override fun onItemClick(position: Int) {
                val orderPosition = currentUser.orders.size-1-position
                OrderSingleton.getInstance().setOrder(currentUser.orders[orderPosition])
                val intentOrderInfo = Intent(context, OrderInfoActivity::class.java)
                startActivity(intentOrderInfo)
            }

        })

        binding.recyclerViewOrderList.layoutManager = rvLayoutManager
        binding.recyclerViewOrderList.adapter = rvAdapter
    }

    /**
     * Listens for any changes made in the database
     */
    private fun listenForDatabaseChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserId = UserSingleton.getInstance().getCurrentUser().userId

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUserId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot!!.exists()) {
                    val updatedUser = documentSnapshot.toObject(User::class.java)
                    UserSingleton.getInstance().setCurrentUser(updatedUser!!)
                    checkUserOrdersList()
                }
            }
    }

    /**
     * Checks the user's orders list, and it will display one layout or another depending on the result
     */
    private fun checkUserOrdersList() {
        if (currentUser.orders.isEmpty()) { // If the user hasn't made any orders
            binding.textViewEmptyOrderList.visibility = View.VISIBLE
            binding.recyclerViewOrderList.visibility = View.GONE
        } else { // If the user has made at least 1 order
            if (binding.recyclerViewOrderList.visibility == View.GONE) {
                binding.recyclerViewOrderList.visibility = View.VISIBLE
                binding.textViewEmptyOrderList.visibility = View.GONE
            }
            rvAdapter?.setOrderList(currentUser.showAllOrders())
        }
    }

}
