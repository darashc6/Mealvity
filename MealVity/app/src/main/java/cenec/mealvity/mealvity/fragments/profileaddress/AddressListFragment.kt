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
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.models.UserModel
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.classes.viewmodels.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import io.sulek.ssml.SSMLLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_address_list.view.*

/**
 * A simple [Fragment] subclass.
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

    private fun setupRecyclerView(context: Context) {
        rvAddressList.layoutManager=SSMLLinearLayoutManager(context)
        rvAdapter=AddressRecyclerViewAdapter(UserModel.getInstance().getCurrentUser().addresses!!)
        rvAdapter.setOnAddressRecyclerViewListener(object : AddressRecyclerViewAdapter.AddressRecyclerViewListener{
            override fun onAddressDelete(position: Int) {
                deleteAddressFromDatabase(position)
            }

            override fun onAddressEdit(position: Int) {
                val intentEditAddress = Intent(context, SaveAddressActivity::class.java)
                val bun = Bundle()
                bun.putInt("position_list", position)
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

    private fun deleteAddressFromDatabase(position: Int) {
        val mFirebaseFirestore = FirebaseFirestore.getInstance()
        val userLoggedIn = UserModel.getInstance().getCurrentUser()
        userLoggedIn.addresses!!.removeAt(position)

        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS)
            .document(userLoggedIn.email!!)
            .update(Constants.FIRESTORE_KEY_DATABASE_USERS_ADDRESSES, userLoggedIn.addresses)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UserModel.getInstance().setCurrentUser(userLoggedIn)
                    userViewModel.setUserLiveData(userLoggedIn)
                    Toast.makeText(context, "Address deleted", Toast.LENGTH_SHORT).show()
                } else {
                    // TODO
                }
            }
    }

}
