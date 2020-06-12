package cenec.mealvity.mealvity.fragments.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.darash.mealvity.R
import cenec.darash.mealvity.databinding.FragmentOrdersTabBinding
import cenec.mealvity.mealvity.activities.OrderInfoActivity
import cenec.mealvity.mealvity.classes.adapters.OrderRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.reservations.Reservation
import cenec.mealvity.mealvity.classes.singleton.OrderSingleton
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.Order
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Fragment of the Orders Tab
 */
class OrdersTabFragment : Fragment() {
    private val currentUser by lazy { UserSingleton.getInstance().getCurrentUser() }
    private var rvAdapter: OrderRecyclerViewAdapter? = null
    private var _binding: FragmentOrdersTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersTabBinding.inflate(inflater, container, false)
        setupRecyclerView()
        listenForDatabaseChanges()
        return binding.root
    }

    private fun setupRecyclerView() {
        val rvLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvAdapter = OrderRecyclerViewAdapter(reverseOrdersList(currentUser.orders))
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

    private fun reverseOrdersList(userOrdersList: ArrayList<Order>): ArrayList<Order> {
        val reversedOrderList = arrayListOf<Order>()

        if (userOrdersList.isNotEmpty()) {
            for (i in userOrdersList.size-1 downTo 0) {
                reversedOrderList.add(userOrdersList[i])
            }
        }

        return reversedOrderList
    }

    private fun listenForDatabaseChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val currentUserId = UserSingleton.getInstance().getCurrentUser().userId

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUserId!!)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot!!.exists()) {
                    val updatedUser = documentSnapshot.toObject(User::class.java)
                    UserSingleton.getInstance().setCurrentUser(updatedUser!!)
                    rvAdapter?.setOrderList(reverseOrdersList(updatedUser.orders))
                }
            }
    }

}
