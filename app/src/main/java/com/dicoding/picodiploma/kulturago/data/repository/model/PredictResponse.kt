package com.dicoding.picodiploma.kulturago.data.repository.model

data class PredictResponse(
    val status: String,
    val data: PredictData
)

data class PredictData(
    val createdAt: String,
    val id: String,
    val keyword: String,
    val predictedPlaces: List<String>,
    val recommendations: List<RecommendedPlace>
)

data class RecommendedPlace(
    val id: Int,
    val place_name: String,
    val description: String,
    val image_link: String
)
