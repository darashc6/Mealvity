package cenec.mealvity.mealvity.classes.interfaceapi

import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HereApi {
    companion object {
        const val API_KEY = "PjfjWvg2utsgPMIRFXJhjiBN21bq4h6KdOKUY4I0hzA"
        const val SEARCH_LIMIT = 5
        const val RESULT_TYPES = "street"
        const val COUNTRY_CODE = "countryCode:ESP"
        const val AT = "13.1,-43.1"
    }

    @GET("autosuggest")
    fun getAutocompleteAddresses(
        @Query("q") query: String,
        @Query("in") countryCode: String = COUNTRY_CODE,
        @Query("at") at: String = AT,
        @Query("resultTypes") resultTypes: String = RESULT_TYPES,
        @Query("limit") searchLimit: Int = SEARCH_LIMIT,
        @Query("apiKey") key: String = API_KEY
    ): Call<StreetList>
}