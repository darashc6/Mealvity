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
 *Fragment containing a list of reservations received by the restaurant
 */
class ReservationListFragment : Fragment(), FragmentContainerActivity.FragmentContainerActivityListener {
    private var dbRestaurant = RestaurantDatabaseSingleton.getInstance().getRestaurantDatabase() // Instance of the restaurant database
    private var _binding: FragmentReservationListBinding? = null // View binding for the fragment
    private val binding get() = _binding!! // Non-nullable version for the binding variable above
    private var rvAdapter: ReservationListRecyclerViewAdapter? = null // Adapter for the RecyclerView
    private var filterOpt = 0 // Option for the filter (0 - All orders, 1 - Pending, 2 - Accepted, 3 - Rejected)
    private val viewModel by lazy { (context as FragmentContainerActivity).getViewModel() } // ViewModel of RestaurantDatabase
    private lateinit var auxReservation: Reservation // Auxiliar variable for Reservations
    private lateinit var reservationUser: User // User who has made the reservations

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

    /**
     * Sets up a RecyclerView, containing a list of reservations
     */
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
                    dbRestaurant.reservations[dbRestaurant.reservations.size-1-position].rejectionReason = reason
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

    /**
     * Updates the changes made to the database
     */
    private fun updateChanges() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("restaurants").document(dbRestaurant.name)
            .set(dbRestaurant).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getReservationUser()
                } else {
                    Toast.makeText(context, "Error updating changes, please try again later", Toast.LENGTH_LONG).show()
                    println(task.exception)
                }
            }
    }

    /**
     * Retrieves the user who made the reservation in order to update that specific user to the database
     */
    private fun getReservationUser() {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()

        mFirebaseFirestore.collection("users").document(auxReservation.user!!.userId!!)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reservationUser = task.result!!.toObject(User::class.java)!!
                    updateUserDatabase()
                } else {
                    Toast.makeText(context, "Error updating changes, please try again later", Toast.LENGTH_LONG)
                        .show()
                    println(task.exception)
                }
            }
    }

    /**
     * Updates the user who made the reservation the the database
     */
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
                    Toast.makeText(context, "Error updating changes, please try again later", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    /**
     * Filters the list depending on the option selected
     */
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
