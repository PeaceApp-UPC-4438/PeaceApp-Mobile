package com.innovatech.peaceapp.Map

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.ReportsActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private var c = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.mapView)

        obtainAllLocations()

        locateCurrentPosition()
        setupMap()

        navigationMenu()
    }

    private fun navigationMenu() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            // check the icon for default with the actual activity

            bottomNavigationView.menu.findItem(R.id.nav_map).setIcon(R.drawable.location_icon)
            bottomNavigationView.menu.findItem(R.id.nav_report).setIcon(R.drawable.reports_icon)
            bottomNavigationView.menu.findItem(R.id.nav_shared_location).setIcon(R.drawable.share_location_icon)

            if(item.isChecked) {
                return@setOnNavigationItemSelectedListener false
            }

            when (item.itemId) {
                R.id.nav_map -> {
                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_report -> {
                    val intent = Intent(this, ListReportsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_shared_location -> {
                    true
                }
                else -> false
            }

        }

        bottomNavigationView.menu.findItem(R.id.nav_map).setChecked(true)
    }

    private fun obtainAllLocations() {
        val service = RetrofitClient.placeHolder
        service.getLocations().enqueue(object: Callback<List<Beans.Location>> {
            override fun onResponse(call: Call<List<Beans.Location>>, response: Response<List<Beans.Location>>) {
                val locations = response.body()

                if (locations != null) {
                    for (location in locations) {
                        addMarker(location.alatitude, location.alongitude)
                    }
                }
            }

            override fun onFailure(call: Call<List<Beans.Location>>, t: Throwable) {
                Log.e("Error MAP", t.message.toString())
            }
        })

    }

    private fun addMarker(latitude: Double, longitude: Double) {

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
        val point = Point.fromLngLat(longitude, latitude)
        val annotationApi = mapView.annotations

        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconSize(0.5)
        pointAnnotationManager.create(pointAnnotationOptions)
    }

    private fun locateCurrentPosition() {
        // verify if the location permission is granted
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        // load the current location in the map
        val locationComponentPlugin: LocationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings { enabled = true }

        locationComponentPlugin.addOnIndicatorPositionChangedListener{ point ->
            val latitude = point.latitude()
            val longitude = point.longitude()

            Log.i("Location", "Latitude: $latitude, Longitude: $longitude")

            // move the camera to the current location
            if (c == 0) {
                mapView.camera.easeTo(
                    CameraOptions.Builder()
                        .center(Point.fromLngLat(longitude, latitude))
                        .zoom(15.0)
                        .build()
                )
                c++
            }
        }


    }

    private fun setupMap() {
        mapView.logo.updateSettings {
            enabled = false
        }

        mapView.compass.updateSettings {
            enabled = false
        }

        // disable scale bar of mapbox on the top left corner of the screen
        mapView.scalebar.updateSettings {
            enabled = false
        }

        // disable the information icon next of mapbox logo
        mapView.attribution.updateSettings {
            enabled = false
        }
    }
}