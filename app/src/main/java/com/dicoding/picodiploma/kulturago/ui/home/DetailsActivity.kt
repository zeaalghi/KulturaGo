package com.dicoding.picodiploma.kulturago.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.data.repository.api.ApiClient
import com.dicoding.picodiploma.kulturago.data.repository.model.PlaceDetailResponse
import com.dicoding.picodiploma.kulturago.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var placeImageView: ImageView
    private lateinit var placeNameTextView: TextView
    private lateinit var placeDescriptionTextView: TextView
    private lateinit var placeTypeTextView: TextView
    private lateinit var priceInformationTextView: TextView
    private lateinit var mapsBtn: Button

    private var placeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize views
        placeImageView = binding.placeImage
        placeNameTextView = binding.placeName
        placeDescriptionTextView = binding.placeDescription
        placeTypeTextView = binding.placeType
        priceInformationTextView = binding.priceInformation
        mapsBtn = binding.mapsButton

        // Get the place ID from the intent
        placeId = intent.getIntExtra("PLACE_ID", -1)

        if (placeId != -1) {
            fetchPlaceDetails(placeId)
        }
    }

    private fun fetchPlaceDetails(id: Int) {
        // Use ApiClient to fetch details for the place
        val apiClient = ApiClient()

        apiClient.getPlaceById(id, object : ApiClient.ApiCallback<PlaceDetailResponse> {
            override fun onSuccess(result: PlaceDetailResponse) {
                val mapLink = "https://www.google.com/maps?q=${result.latitude},${result.longitude}"
                // Update UI with the fetched data
                placeNameTextView.text = result.nama
                placeDescriptionTextView.text = result.description
                placeTypeTextView.text = result.types
                priceInformationTextView.text = "Weekday: ${result.htm_weekday} | Weekend: ${result.htm_weekend}"

                mapsBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapLink))
                    startActivity(intent)
                }

                Glide.with(this@DetailsActivity)
                    .load(result.image_link)
                    .into(placeImageView)
            }

            override fun onFailure(error: String) {
                // Handle error if the API call fails
                // You can show a Toast or some error message here
            }
        })
    }
}
