package com.dreampany.map.ui.activity

import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.afollestad.materialdialogs.MaterialDialog
import com.dreampany.map.R
import com.dreampany.map.data.model.GooglePlace
import com.dreampany.map.manager.PlaceManager
import com.dreampany.map.misc.Constants
import com.dreampany.map.misc.MarkerRender
import com.dreampany.map.misc.attachSnapHelperWithListener
import com.dreampany.map.ui.adapter.PlaceAdapter
import com.dreampany.map.ui.adapter.SnapOnScrollListener
import com.dreampany.map.ui.model.MarkerItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.common.collect.Maps
import com.google.maps.android.clustering.ClusterManager
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback, PlaceManager.PlaceCallback,
    PlaceAdapter.OnItemClickListener {

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var cluster: ClusterManager<MarkerItem>

    private var location: Location? = null

    private val htb = com.dreampany.map.data.model.Location(33.0860, 129.7884)
    private val DEFAULT_ZOOM: Int = 14
    private val PLACE_TICK_ZOOM: Int = 16

    private val MOON_MAP_URL_FORMAT =
        "https://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg"
    private val TERRAIN_TILES =
        "https://api.maptiler.com/tiles/terrain-quantized-mesh/%d/%d/%d.quantized-mesh-1.0?key=g3QtLjoUDXTnW466k3VQ"
    private val HILLSHADES_TILES =
        "https://api.maptiler.com/tiles/hillshades/%d/%d/%d.png?key=g3QtLjoUDXTnW466k3VQ"
    private val TOPO = "https://api.maptiler.com/maps/topo/{z}/{x}/{y}.png?key=g3QtLjoUDXTnW466k3VQ"
    private val HTB =
        "https://api.maptiler.com/tiles/49f8e0f6-0ccd-4e9f-9f4f-0789bd1f675a/%d/%d/%d.png?key=2bWoH1oxlfyPw6ComaQm"

    private lateinit var tiles: TileOverlay

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: PlaceAdapter

    private var mapReady: Boolean = false
    private val places = Maps.newConcurrentMap<LatLng, GooglePlace>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        initUi()
        initRecycler()
        requestPermission()
        loadMap()
    }

    override fun onDestroy() {
        tiles.clearTileCache()
        super.onDestroy()
    }

    override fun onMapReady(map: GoogleMap) {
        mapReady = true
        initMap(map)
    }

    override fun onPlaces(places: List<GooglePlace>) {
        Timber.v("Places %d", places.size)
        places.forEach { place ->
            //val item = MarkerItem(place.geometry.location.toLatLng(), place.name, "", null)
            //cluster.addItem(item)
            PlaceManager.loadPhoto(placesClient, place.placeId, this)
        }
    }

    override fun onPlacePhoto(place: GooglePlace, bitmap: Bitmap) {
        val item = MarkerItem(place.geometry.location.toLatLng(), place.name, "", bitmap)
        cluster.addItem(item)
        adapter.add(place)
        places.put(place.geometry.location.toLatLng(), place)
    }

    override fun onItemClick(item: GooglePlace) {
        moveCamera(item.geometry.location)
    }

    private fun initUi() {
        recycler = findViewById(R.id.recycler)

        placesClient = Places.createClient(this)
        locationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.setSmoothScrollbarEnabled(true)
        adapter = PlaceAdapter(this)
        recycler.layoutManager = layoutManager
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        recycler.adapter = adapter
        recycler.attachSnapHelperWithListener(
            PagerSnapHelper(),
            SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
            object : SnapOnScrollListener.OnSnapPositionChangeListener {
                override fun onSnapPositionChange(position: Int) {
                    if (mapReady) {
                        adapter.getItem(position)?.run {
                            moveCamera(this.geometry.location)
                        }
                    }
                }

            }
        )
    }

    private fun initMap(map: GoogleMap) {
        this.map = map
        //map.setMapType(GoogleMap.MAP_TYPE_NONE)
        cluster = ClusterManager<MarkerItem>(this, map)
        cluster.renderer = MarkerRender(applicationContext, map, cluster)
        map.setOnCameraIdleListener(cluster)
        map.setOnMarkerClickListener(cluster)
        map.setOnMapLongClickListener(this::performLongPressOnMarker)

       // map.s

        val tileProvider: TileProvider = object : UrlTileProvider(512, 512) {
            @Synchronized
            override fun getTileUrl(
                x: Int,
                y: Int,
                zoom: Int
            ): URL { // The moon tile coordinate system is reversed.  This is not normal.
                //val reversedY = (1 shl zoom) - attr.y - 1
                val s: String = String.format(Locale.US, HTB, zoom, x, y)
                Timber.v("Tile Url - %s", s)
                var url: URL? = null
                try {
                    url = URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
                return url
            }
        }

        tiles = map.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))
        updateLocation()
    }

    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun hasPermission(): Boolean {
        return isAllGranted(Permission.ACCESS_FINE_LOCATION)
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
        //map.addGroundOverlay(newarkMap)


        var bitmap: Bitmap? = Constants.Api.getBitmapMarker(
            this,
            R.drawable.ic_tutlip,
            "KDDI HTB"
        )

        bitmap = Constants.Api.resize(bitmap, 150, 150)
        val item = MarkerItem(location.toLatLng(), "KDDI HTB", "", bitmap)
        cluster.addItem(item)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location.toLatLng(),
                DEFAULT_ZOOM.toFloat()
            )
        )
        PlaceManager.nearbyPlaces(location, this)
    }

    private fun moveCamera(location: com.dreampany.map.data.model.Location) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location.toLatLng(),
                PLACE_TICK_ZOOM.toFloat()
            )
        )
    }

    private fun performLongPressOnMarker(latLng: LatLng) {

        val dialog = MaterialDialog(this)
            .title(text = latLng.toString())
            //.message(R.string.your_message)

        dialog.show()
/*        var nearest = places.keys.first()
        places.keys.forEach {
            if (Constants.Api.distance(latLng, nearest) > Constants.Api.distance(latLng, it)) {
                nearest = it
            }
        }
        val place: GooglePlace = places.get(nearest)!!
        Timber.v("Marker %s [%s-%s]", place.name, nearest.latitude, nearest.longitude)*/
    }
}
