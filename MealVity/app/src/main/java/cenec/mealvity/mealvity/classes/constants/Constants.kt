package cenec.mealvity.mealvity.classes.constants

import cenec.mealvity.mealvity.classes.user.User

object Constants {
    const val FIRESTORE_KEY_DATABASE_USERS = "users"
    const val FIRESTORE_KEY_DATABASE_USERS_FULL_NAME = "fullName"
    const val FIRESTORE_KEY_DATABASE_USERS_PHONE_NUMBER = "phoneNumber"
    const val FIRESTORE_KEY_DATABASE_USERS_EMAIL = "email"
    const val FIRESTORE_KEY_DATABASE_USERS_PASSWORD = "password"
    const val FIRESTORE_KEY_DATABASE_USERS_ADDRESSES = "addresses"
    const val FIRESTORE_KEY_DATABASE_USERS_ORDERS = "orders"
    const val BUNDLE_KEY_CURRENT_USER = "currentUser"
    var currentUser: User? = null
}