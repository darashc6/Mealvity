package cenec.mealvity.mealvity.fragments.profileaddress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.activities.SaveAddressActivity
import cenec.mealvity.mealvity.activities.UserAddressesActivity
import cenec.mealvity.mealvity.classes.adapters.AddressRecyclerViewAdapter
import cenec.mealvity.mealvity.classes.constants.BundleKeys
import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.classes.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.sulek.ssml.SSMLLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_address_list.view.*

/**
 * Fragment containing a list of all the addresses saved by the user
 */
class AddressListFragment : Fragment() {
    private lateinit var fragmentView: View
    private lateinit var rvAddressList: RecyclerView
    private lateinit var rvAdapter: AddressRecyclerViewAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentView=LayoutInflater.from(context).inflate(R.layout.fragment_address_list, null)
        rvAddressList=fragmentView.recycler_view_address_list
        userViewModel=(activity as UserAddressesActivity).getUserViewModel()
        setupRecyclerView(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return fragmentView
    }

    /**
     * Sets up the RecyclerView
     * @param context Application context
     */
    private fun setupRecyclerView(context: Context) {
        rvAddressList.layoutManager=SSMLLinearLayoutManager(context)
        rvAdapter=AddressRecyclerViewAdapter(UserSingleton.getInstance().getCurrentUser().addresses)
        rvAdapter.setOnAddressRecyclerViewListener(object : AddressRecyclerViewAdapter.AddressRecyclerViewListener{
            override fun onAddressDelete(position: Int) {
                deleteAddressFromDatabase(position)
            }

            override fun onAddressEdit(position: Int) {
                val intentEditAddress = Intent(context, SaveAddressActivity::class.java)
                val bun = Bundle()
                bun.putInt(BundleKeys.ADDRESS_LIST_POSITION, position)
                intentEditAddress.putExtras(bun)
                startActivity(intentEditAddress)
            }

        })
        rvAddressList.adapter=rvAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel.getUserLiveData().observe(viewLifecycleOwner, object : Observer<User>{
            override fun onChanged(t: User?) {
                rvAdapter.notifyDataSetChanged()
                rvAddressList.adapter=rvAdapter
            }

        })
    }

    /**
     * Deletes an address from the user's list given the list's position
     * @param position Index of the list
     */
    private fun deleteAddressFromDatabase(position: Int) {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userLoggedIn = UserSingleton.getInstance().getCurrentUser()
        userLoggedIn.addresses.removeAt(position)

        mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS)
            .document(firebaseUser!!.uid)
            .update(Database.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES, userLoggedIn.addresses)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UserSingleton.getInstance().setCurrentUser(userLoggedIn)
                    userViewModel.setUserLiveData(userLoggedIn)
                    Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show()
                } else {
                    // TODO
                }
            }
    }

}
