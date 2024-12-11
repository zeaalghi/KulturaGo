import com.dicoding.picodiploma.kulturago.data.repository.model.MostVisitedCulturalPlace
import com.dicoding.picodiploma.kulturago.data.repository.model.NearestCulturalPlacesRequest
import com.dicoding.picodiploma.kulturago.data.repository.model.NearestCulturalPlacesResponse
import com.dicoding.picodiploma.kulturago.data.repository.model.PlaceDetailResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/api/nearest")
    fun getNearestCulturalPlaces(@Body request: NearestCulturalPlacesRequest?): Call<NearestCulturalPlacesResponse?>?

    @GET("/api/place/{id}")
    fun getPlaceById(@Path("id") id: Int): Call<PlaceDetailResponse?>?

    @GET("/api/mostVisited")
    fun getMostVisitedCulturalPlaces(): Call<List<MostVisitedCulturalPlace>>?
}