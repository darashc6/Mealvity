package cenec.mealvity.mealvity.classes.interfaceapi

import cenec.mealvity.mealvity.classes.constants.ApiAccess
import cenec.mealvity.mealvity.classes.restaurant.RestaurantList
import cenec.mealvity.mealvity.classes.restaurant.RestaurantMoreInfo
import retrofit2.Call
import retrofit2.http.*

/**
 * Interface containing functions used with the Yelp Fusion API
 * Link for documentation: https://www.yelp.com/developers/documentation/v3
 */
interface YelpFusionApi {

    /**
     * Constant parameters used for querying
     */
    companion object {
        private const val RADIUS_LIMIT = 1000
        private const val SEARCH_LIMIT = 50
        private const val CATEGORY_TYPE = "restaurant"
        private const val API_KEY = "FXShpb7qM3mOuSeapOdzfjteJgEWNAE47onesKNfsj41KTgpFN9F31MYrwXJKIXW4OaZodmvxe-waIIf6diK0brE8ZddH-4NnX_dz7PWrxTJHWhVkleOC5ke2YuhXnYx"
    }

    /**
     * Returns a list of restaurant near the address specified
     * @param location String with the address specified
     * @param radius Max distance of the radius
     * @param limit Max search results limit
     * @param categories Type of category (In this case, restaurant)
     * @return Call with a list of Restaurant
     */
    @GET("businesses/search")
    @Headers("Authorization: Bearer $API_KEY")
    fun getRestaurantListingByAddress(
        @Query("location") location: String,
        @Query("radius") radius: Int = RADIUS_LIMIT,
        @Query("limit") limit: Int = SEARCH_LIMIT,
        @Query("categories") categories: String = CATEGORY_TYPE
    ): Call<RestaurantList>

    /**
     * Returns a list of restaurants with the specified category and address
     * @param location String with the address specified
     * @param categories Type of category
     * @param radius Max distance of the radius
     * @param limit Max search results limit
     * @return Call with a list of Restaurant
     */
    @GET("businesses/search")
    @Headers("Authorization: Bearer $API_KEY")
    fun getRestaurantListByCategory(
        @Query("location") location: String,
        @Query("categories") categories: String,
        @Query("radius") radius: Int = RADIUS_LIMIT,
        @Query("limit") limit: Int = SEARCH_LIMIT
    ): Call<RestaurantList>

    /**
     * Returns a list of restaurants with the filters applied
     * @param location String with the address specified
     * @param extras HashMap with the filters to apply
     * @param categories Type of category (In this case, restaurant)
     * @param limit Max search results limit
     * @return Call with a list of Restaurant
     */
    @GET("businesses/search")
    @Headers("Authorization: Bearer $API_KEY")
    fun getRestaurantListByCustomParameters(
        @Query("location") location: String,
        @QueryMap extras: HashMap<String, String>,
        @Query("categories") categories: String = CATEGORY_TYPE,
        @Query("limit") limit: Int = SEARCH_LIMIT
    ): Call<RestaurantList>

    /**
     * Returns a RestaurantMoreInfo object, containing extra info about the restaurant
     * @param restaurantId Yelp's id of the restaurant
     * @return Call with RestaurantMoreInfo
     */
    @GET("businesses/{id}")
    @Headers("Authorization: Bearer $API_KEY")
    fun getRestaurantInfoById(
        @Path("id") restaurantId: String
    ): Call<RestaurantMoreInfo>
}