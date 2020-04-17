package cenec.mealvity.mealvity.classes.models

import cenec.mealvity.mealvity.classes.user.User

class UserModel {
    companion object {
        private var mUserInstance: UserModel? = null
        private var mUserListener: UserModelListener? = null
        private var currentUser: User? = null

        fun getInstance(): UserModel {
            if (mUserInstance == null) {
                mUserInstance = UserModel()
            }
            return mUserInstance!!
        }
    }

    fun setCurrentUser(updatedUser: User) {
        currentUser = updatedUser
        if (mUserListener != null) {
            mUserListener!!.onUserUpdate(currentUser!!)
        }
    }

    fun getCurrentUser(): User {
        return currentUser!!
    }

    fun setUserModelListener(listener: UserModelListener) {
        mUserListener = listener
    }

    interface UserModelListener {
        fun onUserUpdate(updatedUser: User)
    }
}