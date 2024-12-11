package com.dicoding.picodiploma.kulturago.data.repository.model


class NearestCulturalPlacesResponse {
    var status: String? = null
    var data: List<PlaceData>? = null

    class PlaceData {
        var nama: String? = null
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var image_link: String? = null
        var distance: Double = 0.0
    }
}
