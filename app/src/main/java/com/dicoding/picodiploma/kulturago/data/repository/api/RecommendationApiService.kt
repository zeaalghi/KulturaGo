import com.dicoding.picodiploma.kulturago.data.repository.model.PredictRequest
import com.dicoding.picodiploma.kulturago.data.repository.model.PredictResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationApiService {
    @POST("/api/predict")
    fun getRecommendedCulturalPlaces(@Body request: PredictRequest): Call<PredictResponse?>?
}
