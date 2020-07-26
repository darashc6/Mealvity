package cenec.mealvity.mealvity.classes.singleton

import cenec.mealvity.mealvity.classes.user.User

/**
 * Class using the singleton model for the User class
 * The object instance will be created only once
 */
class UserSingleton {
    companion object {
        private var mUserInstance: UserSingleton? = null // Instance of the class
        private var mUserListener: UserSingletonListener? = null // Listener for the class
        private var currentUser: User? = null // User of the instance

        /**
         * Returns the instance of the class
         * @return Instance of the class
         */
        fun getInstance(): UserSingleton {
            if (mUserInstance == null) {
                mUserInstance = UserSingleton()
            }
            return mUserInstance!!
        }
    }

    /**
     * Updates the data of the user of the instance
     * @param updatedUser User with the updated data
     */
    fun setCurrentUser(updatedUser: User) {
        currentUser = updatedUser
        mUserListener?.onUserUpdate(updatedUser)
    }

    /**
     * Returns the user of the instance
     * @return User of the instance
     */
    fun getCurrentUser(): User {
        return currentUser!!
    }

    /**
     * Sets the listener of the instance
     * @param listener listener of the instance
     */
    fun setUserModelListener(listener: UserSingletonListener) {
        mUserListener = listener
    }

    /**
     * Interface of the class
     */
    interface UserSingletonListener {
        /**
         * Updates the user of the instance
         * @param updatedUser User with the updated data
         */
        fun onUserUpdate(updatedUser: User)
    }
}