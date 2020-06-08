package cenec.mealvity.mealvity.classes.config

import cenec.mealvity.mealvity.classes.constants.Database
import cenec.mealvity.mealvity.classes.exceptions.MustImplementListenerException
import cenec.mealvity.mealvity.classes.singleton.UserSingleton
import cenec.mealvity.mealvity.classes.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseConfig {
    companion object {
        private var listener: DatabaseConfigListener? = null

        fun updateUserInDatabase() {
            if (listener != null) {
                val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
                val currentUser = UserSingleton.getInstance().getCurrentUser()
                val mFirebaseFirestore = FirebaseFirestore.getInstance()

                mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentFirebaseUser!!.uid)
                    .set(currentUser).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listener!!.onTaskSuccessful()
                        } else {
                            listener!!.onTaskFailed()
                        }
                    }
            } else {
                throw MustImplementListenerException("Activity/Fragment MUST implement DatabaseConfigListener")
            }
        }

        fun listenForUserChanges() {
            if (listener != null) {
                val mFirebaseFirestore = FirebaseFirestore.getInstance()
                val currentUserId = UserSingleton.getInstance().getCurrentUser().userId

                mFirebaseFirestore.collection(Database.FIRESTORE_KEY_DATABASE_USERS).document(currentUserId!!)
                    .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        if (documentSnapshot!!.exists()) {
                            val updatedUser = documentSnapshot.toObject(User::class.java)
                            listener!!.onUserUpdated(updatedUser!!)
                        }
                    }
            } else {
                throw MustImplementListenerException("Activity/Fragment MUST implement DatabaseConfigListener")
            }
        }

        fun setDatabaseConfigListener(newListener: DatabaseConfigListener) {
            listener = newListener
        }
    }

    interface DatabaseConfigListener{
        fun onTaskSuccessful()
        fun onTaskFailed()
        fun onUserUpdated (userUpdated: User)
    }
}