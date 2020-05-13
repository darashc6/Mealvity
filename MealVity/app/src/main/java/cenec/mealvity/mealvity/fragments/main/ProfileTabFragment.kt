package cenec.mealvity.mealvity.fragments.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.activities.*
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import cenec.mealvity.mealvity.classes.viewmodels.UserViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

/**
 * Frgament of the profile tab
 */
class ProfileTabFragment : Fragment() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() } // Instance of Authetication of Firebase
    private lateinit var fragmentView: View // View of the fragment
    private lateinit var tvProfileName: TextView // TextView of the profile name
    private lateinit var tvProfileEmail: TextView // TextView of the profile email
    private lateinit var ivProfilePhoto: ImageView // ImageView of the profile photo
    private lateinit var cvAccountInfo: CardView // Button for showoing the account info
    private lateinit var cvChangePassword: CardView // Button for changing the password
    private lateinit var cvUserAddresses: CardView // Button for showing the address list of the user
    private lateinit var cvNotifications: CardView // Button for showing the notification preferences
    private lateinit var cvHelp: CardView // Button for showing an app guide for the user
    private val userLoggedIn by lazy { UserSingleton.getInstance().getCurrentUser() } // Instance of the user currently logged in
    private lateinit var userViewModel: UserViewModel // ViewModel of the User class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isUserAccountGoogle=mFirebaseAuth.currentUser!!.
            getIdToken(false).result!!.signInProvider.equals("google.com", true)
        userViewModel=(activity as FragmentContainerActivity).getUserViewModel()
        // the layout of the fragment will depend on whether the user has logged in via Google or E-Mail/Password
        fragmentView = if (!isUserAccountGoogle) {
            LayoutInflater.from(context).inflate(R.layout.fragment_profile_tab, null)
        } else {
            LayoutInflater.from(context).inflate(R.layout.fragment_profile_tab_google, null)
        }

        val profilePhoto=mFirebaseAuth.currentUser!!.photoUrl
        tvProfileEmail=fragmentView.findViewById(R.id.textview_profile_email)
        tvProfileName=fragmentView.findViewById(R.id.textview_profile_name)
        ivProfilePhoto=fragmentView.findViewById(R.id.profile_logo)

        cvAccountInfo=fragmentView.findViewById(R.id.cardview_account_info)
        cvUserAddresses=fragmentView.findViewById(R.id.cardView_delivery_addresses)
        cvNotifications=fragmentView.findViewById(R.id.cardView_notifiacions)
        cvHelp=fragmentView.findViewById(R.id.cardView_help)

        tvProfileEmail.text=userLoggedIn.email
        tvProfileName.text=userLoggedIn.fullName
        if (profilePhoto!=null) {
            Glide.with(this).load(profilePhoto).into(ivProfilePhoto)
        }

        cvAccountInfo.setOnClickListener {
            newActivity(EditProfileActivity::class.java)
        }

        //  If user is logged in using Google, he won't be able to change his password
        if (!isUserAccountGoogle) {
            cvChangePassword=fragmentView.findViewById(R.id.cardView_change_password)
            cvChangePassword.setOnClickListener{
                newActivity(ChangePasswordActivity::class.java)
            }
        }

        cvUserAddresses.setOnClickListener {
            newActivity(UserAddressesActivity::class.java)
        }

        cvNotifications.setOnClickListener {
            newActivity(NotificationsActivity::class.java)
        }

        cvHelp.setOnClickListener {
            newActivity(HelpActivity::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return fragmentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userViewModel.getUserLiveData().observe(viewLifecycleOwner, object : Observer<User>{
            override fun onChanged(updatedUser: User?) {
                updatedUser?.let {
                    tvProfileName.text = it.fullName
                    tvProfileEmail.text = it.email
                }
            }

        })
    }

    /**
     * Launches a new activity to the class specified by parameter
     * @param className Class of the activity
     */
    private fun newActivity(className: Class<*>) {
        val intentNewActivity=Intent(context, className)
        startActivity(intentNewActivity)
    }

}
