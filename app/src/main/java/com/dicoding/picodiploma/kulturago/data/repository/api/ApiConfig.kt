package com.dicoding.picodiploma.kulturago.data.repository.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://notes-api-683772643342.asia-southeast2.run.app/"
    private const val RECOMMEND_BASE_URL = "https://backend-py-683772643342.asia-southeast2.run.app/"

    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val recommendationRetrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(RECOMMEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
