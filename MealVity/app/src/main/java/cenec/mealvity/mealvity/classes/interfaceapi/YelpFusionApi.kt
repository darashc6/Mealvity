package cenec.mealvity.mealvity.classes.interfaceapi

import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.restaurant.RestaurantList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface YelpFusionApi {


    /**
     * Returns a list of restaurant near the address specified
     * Link: https://api.yelp.com/v3/businesses/search?location=Calle+Centurion+y+Cordoba%2C+10&radius=1500&limit=50&categories=restaurants
     */
    @GET("businesses/search")
    @Headers("Authorization: Bearer ${ApiAccess.API_KEY_YELP_FUSION_API}")
    fun getRestaurantListingByAddress(
        @Query("location") location: String,
        @Query("radius") radius: Int = 1500,
        @Query("limit") limit: Int = 50,
        @Query("categories") categories: String = "restaurants"
    ): Call<RestaurantList>
}