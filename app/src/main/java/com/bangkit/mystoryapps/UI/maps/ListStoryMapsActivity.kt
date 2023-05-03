package com.bangkit.mystoryapps.UI.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bangkit.mystoryapps.R
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bangkit.mystoryapps.databinding.ActivityListStoryMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions

class ListStoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListStoryMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapStory) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewModel: StoryViewModel by viewModels {
            factory
        }
        viewModel.getAllStoryList().observe(this){result->
            if(result != null){
                when(result){
                    is Result.Loading -> {

                    }
                    is Result.Error -> {
                        Toast.makeText(this, "Error map: ${result.error}", Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        for(story in result.data){
                            val position = LatLng(story.latitude!!.toDouble(), story.longitude!!.toDouble())
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(story.name)
                                    .snippet(story.description)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            )
                        }
                    }
                }
            }
        }
        getMyLocation()
        setMapStyle()
    }
    private fun setMapStyle(){
        try{
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if(!success){
                Log.e("MapActivity", "Map style gagal di parse, kesalahan internal")
            }
        } catch (e: Resources.NotFoundException){
            Log.e("MapActivity", "Map style gagal di parse, pesan kesalahan: $e")
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation(){
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}