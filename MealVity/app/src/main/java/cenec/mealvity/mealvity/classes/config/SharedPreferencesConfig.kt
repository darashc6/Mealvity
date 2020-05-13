package cenec.mealvity.mealvity.classes.config

import android.content.Context
import cenec.darash.mealvity.R

/**
 * Class used for storing data in Preferences
 * @param appContext Application context
 */
class SharedPreferencesConfig(private var appContext: Context) {
    private var prefs = appContext.getSharedPreferences(appContext.resources.getString(R.string.package_name), Context.MODE_PRIVATE) // App's Preferences

    /**
     * Saves the default street in the app's preferences
     * @param streetToSave Street to save in the preferences
     */
    fun saveDefaultStreet(streetToSave: String) {
        val editor = prefs.edit()
        editor.putString(appContext.getString(R.string.preferences_default_search_street), streetToSave)
        editor.apply()
    }

    /**
     * Returns the default street if previously stored in preferences
     * @return Default street if previously saved in preferences, else it will return an empty string
     */
    fun getDefaultStreet(): String {
        prefs.getString(appContext.getString(R.string.preferences_default_search_street), "")?.let {
            return it
        }
        return ""
    }
}