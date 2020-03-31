package cenec.mealvity.mealvity.fragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cenec.darash.mealvity.R
import cenec.mealvity.mealvity.classes.constants.Constants
import cenec.mealvity.mealvity.classes.user.User
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile_tab.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class ProfileTabFragment : Fragment() {
    private val mFirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var etProfileName: TextView
    private lateinit var etProfileEmail: TextView
    private lateinit var ivProfilePhoto: ImageView
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_profile_tab, container, false)
        val userEmail = mFirebaseAuth.currentUser!!.email.toString()
        val profilePhoto = mFirebaseAuth.currentUser!!.photoUrl
        etProfileEmail = fragmentView.findViewById(R.id.textview_profile_email)
        etProfileName = fragmentView.findViewById(R.id.textview_profile_name)
        ivProfilePhoto = fragmentView.findViewById(R.id.profile_logo)

        mFirebaseFirestore.collection(Constants.FIRESTORE_KEY_DATABASE_USERS).document(userEmail).get()
            .addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                override fun onComplete(task: Task<DocumentSnapshot>) {
                    if (task.isSuccessful) {
                        currentUser = task.result!!.toObject(User::class.java)
                        etProfileName.text = currentUser!!.fullName
                        etProfileEmail.text = currentUser!!.email
                        if (profilePhoto!=null) {
                            Glide.with(context!!).load(profilePhoto).into(ivProfilePhoto)
                        }
                    } else {
                        Toast.makeText(context, task.exception!!.message, Toast.LENGTH_LONG).show()
                        Log.d("ErrorData", "${task.exception!!.message}")
                    }
                }

            })

        return fragmentView
    }

}
