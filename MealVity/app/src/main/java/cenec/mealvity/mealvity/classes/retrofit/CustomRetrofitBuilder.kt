package cenec.mealvity.mealvity.classes.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomRetrofitBuilder {
    companion object {
        fun createRetrofitBuilder(urlString: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(urlString)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}