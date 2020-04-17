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
 * A simple [Fragment] subclass.
 */
class ProfileTabFragment : Fragment() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var fragmentView: View
    private lateinit var etProfileName: TextView
    private lateinit var etProfileEmail: TextView
    private lateinit var ivProfilePhoto: ImageView
    private lateinit var cvAccountInfo: CardView
    private lateinit var cvChangePassword: CardView
    private lateinit var cvUserAddresses: CardView
    private lateinit var cvNotifications: CardView
    private lateinit var cvHelp: CardView
    private lateinit var currentUser: User
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser=UserSingleton.getInstance().getCurrentUser()
        val isUserAccountGoogle=FirebaseAuth.getInstance().currentUser!!.
            getIdToken(false).result!!.signInProvider.equals("google.com", true)
        userViewModel=(activity as FragmentContainerActivity).getUserViewModel()
        fragmentView = if (!isUserAccountGoogle) {
            LayoutInflater.from(context).inflate(R.layout.fragment_profile_tab, null)
        } else {
            LayoutInflater.from(context).inflate(R.layout.fragment_profile_tab_google, null)
        }

        val profilePhoto=mFirebaseAuth.currentUser!!.photoUrl
        etProfileEmail=fragmentView.findViewById(R.id.textview_profile_email)
        etProfileName=fragmentView.findViewById(R.id.textview_profile_name)
        ivProfilePhoto=fragmentView.findViewById(R.id.profile_logo)

        cvAccountInfo=fragmentView.findViewById(R.id.cardview_account_info)
        cvUserAddresses=fragmentView.findViewById(R.id.cardView_delivery_addresses)
        cvNotifications=fragmentView.findViewById(R.id.cardView_notifiacions)
        cvHelp=fragmentView.findViewById(R.id.cardView_help)

        etProfileEmail.text=currentUser.email
        etProfileName.text=currentUser.fullName
        if (profilePhoto!=null) {
            Glide.with(this).load(profilePhoto).into(ivProfilePhoto)
        }

        cvAccountInfo.setOnClickListener {
            newActivity(EditProfileActivity::class.java)
        }

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
                if (updatedUser!=null) {
                    etProfileName.text = updatedUser.fullName
                    etProfileEmail.text = updatedUser.email
                }
            }

        })
    }


    private fun newActivity(className: Class<*>) {
        val intentNewActivity=Intent(context, className)
        startActivity(intentNewActivity)
    }

}
