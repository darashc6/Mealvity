package cenec.mealvity.mealvity.classes.interfaceapi

import cenec.mealvity.mealvity.classes.autocompleteaddress.StreetList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface containing functions used with the HERE REST APIs
 * Link for documentation: https://developer.here.com/develop/rest-apis
 */
interface HereApi {
    /**
     * Constant parameters used for querying
     */
    companion object {
        private const val API_KEY = "PjfjWvg2utsgPMIRFXJhjiBN21bq4h6KdOKUY4I0hzA"
        private const val SEARCH_LIMIT = 5
        private const val RESULT_TYPES = "street"
        private const val COUNTRY_CODE = "countryCode:ESP"
        private const val AT = "13.1,-43.1"
    }

    /**
     * Returns a list of autosuggeted Street depending on the query
     * @param query Search text
     * @param countryCode 3 digit country code of the search location
     * @param at Center of the search context
     * @param resultTypes Type of result the autosuggest shoudl return (In this case, street)
     * @param searchLimit Max limit of results returned
     * @param key API key
     * @return Call with a list of Street
     */
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