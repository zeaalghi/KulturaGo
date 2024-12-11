package com.dicoding.picodiploma.kulturago.ui.home.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.kulturago.R
import com.dicoding.picodiploma.kulturago.data.repository.api.ApiClient
import com.dicoding.picodiploma.kulturago.data.repository.model.NearestCulturalPlacesResponse
import com.dicoding.picodiploma.kulturago.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
                enableLocationUI()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMapsBinding.bind(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (checkLocationPermission()) {
            getMyLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocationUI() {
        if (checkLocationPermission()) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkLocationPermission()) {
            getMyLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getMyLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    showNearbyCulturalPlaces(location)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                } else {
                    Toast.makeText(requireContext(), "Location not found. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showNearbyCulturalPlaces(currentLocation: Location) {
        mMap.clear()

        val apiClient = ApiClient()
        apiClient.getNearestCulturalPlaces(
            currentLocation.latitude,
            currentLocation.longitude,
            object : ApiClient.ApiCallback<NearestCulturalPlacesResponse> {
                override fun onSuccess(result: NearestCulturalPlacesResponse) {
                    result.data?.forEach { place ->
                        val poiLatLng = LatLng(place.latitude, place.longitude)
                        val marker = mMap.addMarker(MarkerOptions().position(poiLatLng).title(place.nama))
                        marker?.tag = place
                    }

                    mMap.setOnInfoWindowClickListener { marker ->
                        val place = marker.tag as? NearestCulturalPlacesResponse.PlaceData
                        place?.let {
                            it.nama?.let { name ->
                                openLocationInGoogleMaps(it.latitude, it.longitude, name)
                            }
                        }
                    }
                }

                override fun onFailure(error: String) {
                    Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun openLocationInGoogleMaps(latitude: Double, longitude: Double, name: String) {
        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($name)"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        enableLocationUI()

        if (checkLocationPermission()) {
            getMyLocation()
        } else {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMap.clear()
    }
}




