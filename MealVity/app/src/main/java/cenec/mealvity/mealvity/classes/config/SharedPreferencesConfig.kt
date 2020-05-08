package cenec.mealvity.mealvity.classes.config

import android.content.Context
import cenec.darash.mealvity.R

class SharedPreferencesConfig(private var appContext: Context) {
    private var prefs = appContext.getSharedPreferences(appContext.resources.getString(R.string.package_name), Context.MODE_PRIVATE)

    fun saveDefaultStreet(streetToSave: String) {
        val editor = prefs.edit()
        editor.putString(appContext.getString(R.string.preferences_default_search_street), streetToSave)
        editor.apply()
    }

    fun getDefaultStreet(): String {
        prefs.getString(appContext.getString(R.string.preferences_default_search_street), "")?.let {
            return it
        }
        return ""
    }
}