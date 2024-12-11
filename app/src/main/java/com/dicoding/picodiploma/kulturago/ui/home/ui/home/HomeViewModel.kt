package com.dicoding.picodiploma.kulturago.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.kulturago.data.repository.api.ApiClient
import com.dicoding.picodiploma.kulturago.data.repository.model.MostVisitedCulturalPlace
import com.dicoding.picodiploma.kulturago.data.repository.model.PredictResponse
import com.dicoding.picodiploma.kulturago.data.repository.model.RecommendedPlace

class HomeViewModel : ViewModel() {

    private val _mostVisitedPlaces = MutableLiveData<List<MostVisitedCulturalPlace>>()
    val mostVisitedPlaces: LiveData<List<MostVisitedCulturalPlace>> get() = _mostVisitedPlaces

    private val _recommendedPlaces = MutableLiveData<List<RecommendedPlace>>()
    val recommendedPlaces: LiveData<List<RecommendedPlace>> get() = _recommendedPlaces

    private val apiClient = ApiClient()

    fun fetchMostVisitedCulturalPlaces() {
        apiClient.getMostVisitedCulturalPlaces(object :
            ApiClient.ApiCallback<List<MostVisitedCulturalPlace>> {
            override fun onSuccess(result: List<MostVisitedCulturalPlace>) {
                _mostVisitedPlaces.value = result
            }

            override fun onFailure(error: String) {
            }
        })
    }

    fun fetchRecommendedPlaces(keyword: String) {
        apiClient.getRecommendedCulturalPlaces(
            keyword,
            object : ApiClient.ApiCallback<PredictResponse> {
                override fun onSuccess(result: PredictResponse) {
                    _recommendedPlaces.value = result.data.recommendations
                }

                override fun onFailure(error: String) {
                }
            })
    }
}
