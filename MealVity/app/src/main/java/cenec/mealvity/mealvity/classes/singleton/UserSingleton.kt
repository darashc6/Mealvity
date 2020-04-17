package cenec.mealvity.mealvity.classes.singleton

import cenec.mealvity.mealvity.classes.user.User

class UserSingleton {
    companion object {
        private var mUserInstance: UserSingleton? = null
        private var mUserListener: UserModelListener? = null
        private var currentUser: User? = null

        fun getInstance(): UserSingleton {
            if (mUserInstance == null) {
                mUserInstance = UserSingleton()
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