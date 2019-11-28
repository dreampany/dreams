package com.dreampany.map

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import timber.log.Timber


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var locationClient: FusedLocationProviderClient
    private var location: Location? = null

    private val DEFAULT_ZOOM:Float = 13f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        init()
        requestPermission()
        loadMap()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        updateLocationUi()
        getDeviceLocation()
        // Add a marker in Sydney and move the camera
/*        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }

    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun hasPermission(): Boolean {
        return isAllGranted(Permission.ACCESS_FINE_LOCATION)
    }

    private fun init() {
        placesClient = Places.createClient(this)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestPermission() {
        if (!hasPermission()) {
            runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
                updateLocationUi()
                getDeviceLocation()
            }
        }
    }

    private fun updateLocationUi() {
        try {
            if (isAllGranted(Permission.ACCESS_FINE_LOCATION)) {
                map.setMyLocationEnabled(true)
                map.getUiSettings().setMyLocationButtonEnabled(true)
            } else {
                map.setMyLocationEnabled(false)
                map.getUiSettings().setMyLocationButtonEnabled(false)
                location = null
                requestPermission()
            }
        } catch (error: SecurityException) {
            Timber.e(error)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (hasPermission()) {
                locationClient.getLastLocation().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        location = task.result
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location!!.getLatitude(),
                                    location!!.getLongitude()
                                ), DEFAULT_ZOOM)
                        )
                    } else {
                        Timber.d(  "Current location is null. Using defaults.")
                        Timber.e(  task.exception)
                        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM))
                        map.getUiSettings().setMyLocationButtonEnabled(false)
                    }
                }
            }
        } catch (error: SecurityException) {
            Timber.e(error)
        }
    }
}
