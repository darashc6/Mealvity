package cenec.mealvity.mealvity.classes.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Class used for creating a Retrofit builder with the specified URL
 */
class CustomRetrofitBuilder {
    companion object {
        /**
         * Creates and returns a Retrofit Builder using the specified URL
         * @param urlString String with the URL for the API
         * @return Retrofit object
         */
        fun createRetrofitBuilder(urlString: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(urlString)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}