package com.dicoding.picodiploma.kulturago.data.repository.api

import ApiService
import RecommendationApiService
import com.dicoding.picodiploma.kulturago.data.repository.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient {

    private val apiService: ApiService = ApiConfig.retrofitInstance.create(ApiService::class.java)
    private val recommendationApiService: RecommendationApiService = ApiConfig.recommendationRetrofitInstance.create(RecommendationApiService::class.java)

    fun getNearestCulturalPlaces(
        lat: Double,
        lon: Double,
        callback: ApiCallback<NearestCulturalPlacesResponse>,
    ) {
        val request = NearestCulturalPlacesRequest(lat, lon)
        apiService.getNearestCulturalPlaces(request)
            ?.enqueue(object : Callback<NearestCulturalPlacesResponse?> {
                override fun onResponse(
                    call: Call<NearestCulturalPlacesResponse?>,
                    response: Response<NearestCulturalPlacesResponse?>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onFailure("Error: " + response.message())
                    }
                }

                override fun onFailure(call: Call<NearestCulturalPlacesResponse?>, t: Throwable) {
                    callback.onFailure("Failure: " + t.message)
                }
            })
    }

    fun getPlaceById(id: Int, callback: ApiCallback<PlaceDetailResponse>) {
        apiService.getPlaceById(id)?.enqueue(object : Callback<PlaceDetailResponse?> {
            override fun onResponse(
                call: Call<PlaceDetailResponse?>,
                response: Response<PlaceDetailResponse?>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onFailure("Error: " + response.message())
                }
            }

            override fun onFailure(call: Call<PlaceDetailResponse?>, t: Throwable) {
                callback.onFailure("Failure: " + t.message)
            }
        })
    }

    fun getMostVisitedCulturalPlaces(callback: ApiCallback<List<MostVisitedCulturalPlace>>) {
        apiService.getMostVisitedCulturalPlaces()?.enqueue(object : Callback<List<MostVisitedCulturalPlace>?> {
            override fun onResponse(
                call: Call<List<MostVisitedCulturalPlace>?>,
                response: Response<List<MostVisitedCulturalPlace>?>,
            ) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onFailure("Error: " + response.message())
                }
            }

            override fun onFailure(call: Call<List<MostVisitedCulturalPlace>?>, t: Throwable) {
                callback.onFailure("Failure: " + t.message)
            }
        })
    }

    fun getRecommendedCulturalPlaces(keyword: String, callback: ApiCallback<PredictResponse>) {
        val request = PredictRequest(keyword)
        recommendationApiService.getRecommendedCulturalPlaces(request)
            ?.enqueue(object : Callback<PredictResponse?> {
                override fun onResponse(call: Call<PredictResponse?>, response: Response<PredictResponse?>) {
                    if (response.isSuccessful && response.body() != null) {
                        callback.onSuccess(response.body()!!)
                    } else {
                        callback.onFailure("Error: " + response.message())
                    }
                }

                override fun onFailure(call: Call<PredictResponse?>, t: Throwable) {
                    callback.onFailure("Failure: " + t.message)
                }
            })
    }

    interface ApiCallback<T> {
        fun onSuccess(result: T)
        fun onFailure(error: String)
    }
}
