package com.example.storyapp2.ui.Maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storyapp2.R
import com.example.storyapp2.databinding.ActivityMapsBinding
import com.example.storyapp2.data.utils.ViewModelFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val boundsBuilder = LatLngBounds.Builder()
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val unitToString: Unit = getUserToken()

        val resultUnitToString: String = unitToString.toString()

        Log.e("Token ane", "token : $resultUnitToString")

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        getUserToken()
        setMapStyle()
        addManyMarker()
    }



    private fun addManyMarker() {
        viewModel.mapsResponse.observe(this) { data ->

            var hasValidMarkers = false

            data.listStory.forEach { story ->
                val lat = story.lat
                val lon = story.lon


                if (lat != null && lon != null) {
                    val latLng = LatLng(lat, lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(story.name)
                            .snippet(story.description)
                    )
                    boundsBuilder.include(latLng)
                    hasValidMarkers = true
                } else{
                    Log.e(TAG, "Invalid latLng: lat=$lat, lon=$lon")
                }
            }

            if (hasValidMarkers) {
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun getUserToken(): Unit {
        viewModel.getUser().observe(this) {

            val token = it.token
            Toast.makeText(this@MapsActivity, "Token $token", Toast.LENGTH_SHORT).show()
            Log.d("My Token", "Token Gw : $token")

            viewModel.getLocation("Bearer $token")

        }
    }

    companion object {
        private const val TAG = "MapsActivity"
        const val EXTRA_TOKEN = "extra_token"
    }
}