package com.dreampany.map.ui

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.dreampany.map.R
import com.dreampany.map.data.model.GooglePlace
import com.dreampany.map.manager.PlaceManager
import com.dreampany.map.misc.Constants
import com.dreampany.map.misc.MarkerRender
import com.dreampany.map.ui.model.MarkerItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.ClusterManager
import timber.log.Timber


class MapActivity : AppCompatActivity(), OnMapReadyCallback, PlaceManager.PlaceCallback {

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var cluster: ClusterManager<MarkerItem>

    private var location: Location? = null

    private val htb = com.dreampany.map.data.model.Location(33.0860, 129.7884)
    private val DEFAULT_ZOOM: Int = 12

    private val MOON_MAP_URL_FORMAT =
        "https://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg"
    private val TERRAIN_TILES =
        "https://api.maptiler.com/tiles/terrain-quantized-mesh/%d/%d/%d.quantized-mesh-1.0?key=g3QtLjoUDXTnW466k3VQ"
    private val HILLSHADES_TILES =
        "https://api.maptiler.com/tiles/hillshades/%d/%d/%d.png?key=g3QtLjoUDXTnW466k3VQ"
    private val TOPO = "https://api.maptiler.com/maps/topo/{z}/{x}/{y}.png?key=g3QtLjoUDXTnW466k3VQ"

    //private lateinit var tiles: TileOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        init()
        requestPermission()
        loadMap()
    }

    override fun onDestroy() {
        //tiles.clearTileCache()
        super.onDestroy()
    }

    override fun onMapReady(map: GoogleMap) {
        initMap(map)
    }

    override fun onPlaces(places: List<GooglePlace>) {
        Timber.v("Places %d", places.size)
        places.forEach { place ->
            val item = MarkerItem(place.geometry.location.toLatLng(), place.name, "", null)
            cluster.addItem(item)
            //PlaceManager.loadPhoto(placesClient, place.placeId, this)
        }
    }

    override fun onPlacePhoto(place: GooglePlace, bitmap: Bitmap) {
        val item = MarkerItem(place.geometry.location.toLatLng(), place.name, "", bitmap)
        cluster.addItem(item)
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

    private fun initMap(map: GoogleMap) {
        this.map = map
        //map.setMapType(GoogleMap.MAP_TYPE_NONE)
        cluster = ClusterManager<MarkerItem>(this, map)
        cluster.renderer = MarkerRender(applicationContext, map, cluster)
        map.setOnCameraIdleListener(cluster)
        map.setOnMarkerClickListener(cluster)

/*        val tileProvider: TileProvider = object : UrlTileProvider(512, 512) {
            @Synchronized
            override fun getTileUrl(
                x: Int,
                y: Int,
                zoom: Int
            ): URL { // The moon tile coordinate system is reversed.  This is not normal.
                val reversedY = (1 shl zoom) - attr.y - 1
                val s: String = String.format(Locale.US, HILLSHADES_TILES, zoom, x, y)
                Timber.v("Tile Url - %s", s)
                var url: URL? = null
                try {
                    url = URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
                return url
            }
        }*/

        //tiles = map.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))
        updateLocation()
    }

    private fun requestPermission() {
        if (!hasPermission()) {
            runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
                updateLocation()
                //getDeviceLocation()
            }
        }
    }

    private fun updateLocation() {
        try {
            if (isAllGranted(Permission.ACCESS_FINE_LOCATION)) {
                updateLocation(htb)
                /*map.addMarker(MarkerOptions().position(htb.toLatLng()).title("HTB"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(htb.toLatLng(), DEFAULT_ZOOM.toFloat()))*/
                //map.setMyLocationEnabled(true)
                //map.getUiSettings().setMyLocationButtonEnabled(true)
            } else {
                //map.setMyLocationEnabled(false)
                //map.getUiSettings().setMyLocationButtonEnabled(false)
                location = null
                requestPermission()
            }
        } catch (error: SecurityException) {
            Timber.e(error)
        }
    }

/*    private fun getDeviceLocation() {
        try {
            if (hasPermission()) {
                locationClient.getLastLocation().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        location = task.result
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(htb.toLatLng(), DEFAULT_ZOOM.toFloat()))
                    } else {
                        Timber.d("Current location is null. Using defaults.")
                        Timber.e(task.exception)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(htb.toLatLng(), DEFAULT_ZOOM.toFloat()))
                       // map.getUiSettings().setMyLocationButtonEnabled(false)
                    }
                }
            }
        } catch (error: SecurityException) {
            Timber.e(error)
        }
    }*/

    private fun updateLocation(location: com.dreampany.map.data.model.Location) {

        val newarkMap = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.ic_htb))
            .transparency(0.0f)
            .position(
                location.toLatLng(),
                Constants.Property.GROUND_OVERLAY_WIDTH,
                Constants.Property.GROUND_OVERLAY_HEIGHT
            )
        map.addGroundOverlay(newarkMap)

        val item = MarkerItem(location.toLatLng(), "KDDI HTB", "", null)
        cluster.addItem(item)

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location.toLatLng(),
                DEFAULT_ZOOM.toFloat()
            )
        )
        PlaceManager.nearbyPlaces(location, this)
    }
}
