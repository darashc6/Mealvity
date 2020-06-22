package cenec.mealvity.mealvityforowners.features.reservationlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cenec.mealvity.mealvityforowners.FragmentContainerActivity
import cenec.mealvity.mealvityforowners.R
import cenec.mealvity.mealvityforowners.RestaurantDatabaseSingleton
import cenec.mealvity.mealvityforowners.core.RestaurantDatabase
import cenec.mealvity.mealvityforowners.core.reservation.Reservation
import cenec.mealvity.mealvityforowners.core.user.User
import cenec.mealvity.mealvityforowners.databinding.FragmentReservationListBinding
import cenec.mealvity.mealvityforowners.features.reservationlist.adapter.ReservationListRecyclerViewAdapter
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 */
class ReservationListFragment : Fragment(), FragmentContainerActivity.FragmentContainerActivityListener {
    private var dbRestaurant = RestaurantDatabaseSingleton.getInstance().getRestaurantDatabase()
    private var _binding: FragmentReservationListBinding? = null
    private val binding get() = _binding!!
    private var rvAdapter: ReservationListRecyclerViewAdapter? = null
    private var filterOpt = 0
    private val viewModel by lazy { (context as FragmentContainerActivity).getViewModel() }
    private lateinit var auxReservation: Reservation
    private lateinit var reservationUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationListBinding.inflate(layoutInflater)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRestaurantDatabase().observe(viewLifecycleOwner, Observer { newRestaurantDatabase ->
            dbRestaurant = newRestaurantDatabase
            filterReservationList()
        })
    }

    private fun setupRecyclerView() {
        val rvLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvAdapter = ReservationListRecyclerViewAdapter(dbRestaurant.reservations)
        rvAdapter!!.setReservationListRecyclerViewAdapterListener(object :
            ReservationListRecyclerViewAdapter.ReservationListRecyclerViewAdapterListener {
            override fun onOptionSelected(
                position: Int,
                updatedStatus: Reservation.ReservationStatus,
                reason: String
            ) {
                if (updatedStatus == Reservation.ReservationStatus.REJECTED) {
                    dbRestaurant.reservations[position].rejectionReason = reason
                }
                dbRestaurant.reservations[dbRestaurant.reservations.size-1-position].reservationStatus = updatedStatus
                auxReservation = dbRestaurant.reservations[dbRestaurant.reservations.size-1-position]
                viewModel.setRestaurantDatabase(dbRestaurant)
                updateChanges()
            }


        })

        binding.reservationListRecyclerView.layoutManager = rvLayoutManager
        binding.reservationListRecyclerView.adapter = rvAdapter
    }

    private fun updateChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("restaurants").document(dbRestaurant.name)
            .set(dbRestaurant).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getReservationUser()
                } else {
                    Toast.makeText(context, "Error in updateChanges()", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    private fun getReservationUser() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("users").document(auxReservation.user!!.userId!!)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reservationUser = task.result!!.toObject(User::class.java)!!
                    updateUserDatabase()
                } else {
                    Toast.makeText(context, "Error in getReservationUser()", Toast.LENGTH_LONG)
                        .show()
                    println(task.exception)
                }
            }
    }

    private fun updateUserDatabase() {
        val userReservationList = reservationUser.reservations
        for (i in 0 until userReservationList.size) {
            if (userReservationList[i].referenceNumber == auxReservation.referenceNumber) {
                userReservationList[i] = auxReservation
                break
            }
        }

        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("users").document(reservationUser.userId!!)
            .update("reservations", userReservationList)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    filterReservationList()
                } else {
                    Toast.makeText(context, "Error in updateUserDatabase()", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun filterReservationList() {
        when (filterOpt) {
            0 -> rvAdapter?.setReservationList(dbRestaurant.showAllReservations())
            1 -> rvAdapter?.setReservationList(dbRestaurant.showPendingReservations())
            2 -> rvAdapter?.setReservationList(dbRestaurant.showAcceptedReservations())
            3 -> rvAdapter?.setReservationList(dbRestaurant.showRejectedReservations())
        }
    }

    override fun onFilterOptionSelected(filterOptSelected: Int) {
        filterOpt = filterOptSelected
        filterReservationList()
    }


}
