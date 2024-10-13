package com.innovatech.peaceapp.Map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.Map.Beans.PropertiesPlace
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.Map.Models.RetrofitClientMapbox
import com.innovatech.peaceapp.Profile.ProfileMainDialogFragment
import com.innovatech.peaceapp.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraChangedCallback
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.dsl.generated.switchCase
import com.mapbox.maps.plugin.LocationPuck
import com.mapbox.maps.plugin.LocationPuck2D
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
import com.mapbox.search.autofill.AddressAutofill
import com.mapbox.search.autofill.AddressAutofillOptions
import com.mapbox.search.autofill.AddressAutofillResult
import com.mapbox.search.autofill.AddressAutofillSuggestion
import com.mapbox.search.autofill.Query
import com.mapbox.search.ui.adapter.autofill.AddressAutofillUiAdapter
import com.mapbox.search.ui.view.CommonSearchViewConfiguration
import com.mapbox.search.ui.view.DistanceUnitType
import com.mapbox.search.ui.view.SearchResultsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var token: String
    private lateinit var txtCurrentLocation: TextView
    private lateinit var addressAutofill: AddressAutofill
    private lateinit var searchLocation: EditText
    private lateinit var searchResultsView: SearchResultsView
    private lateinit var addressAutofillUiAdapter: AddressAutofillUiAdapter
    private var ignoreNextQueryTextUpdate: Boolean = false
    private lateinit var mapPin: View
    private var contCallAutoFill: Int = 0
    private var coordinatesCurrentLocation: Point = Point.fromLngLat(0.0, 0.0)
    private var currentLocation: String = ""
    private var ignoreNextMapIdleEvent: Boolean = false
    private var isUserInteracting: Boolean = false
    private lateinit var userProfile: ImageView
    private var c: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        addressAutofill = AddressAutofill.create(locationProvider = null)
        searchLocation = findViewById(R.id.searchLocation)
        searchResultsView = findViewById(R.id.search_results_view)
        mapPin = findViewById(R.id.map_pin)
        txtCurrentLocation = findViewById(R.id.currentLocation)
        token = intent.getStringExtra("token")!!
        mapView = findViewById(R.id.mapView)
        userProfile = findViewById(R.id.userPhoto)
        userProfile.setOnClickListener {
            val profileDialog = ProfileMainDialogFragment()
            profileDialog.show(supportFragmentManager, "ProfileDialogFragment")
        }


        searchResultsView.initialize(
            SearchResultsView.Configuration(
                commonConfiguration = CommonSearchViewConfiguration(DistanceUnitType.IMPERIAL)
            )
        )

        addressAutofillUiAdapter = AddressAutofillUiAdapter(view = searchResultsView, addressAutofill = addressAutofill)
        addressAutofillUiAdapter.addSearchListener(object :
            AddressAutofillUiAdapter.SearchListener {
            override fun onSuggestionSelected(suggestion: AddressAutofillSuggestion) {
                /* when is selected a suggestion, the address is shown in the search bar */
                selectSuggestion(suggestion)
            }
            override fun onSuggestionsShown(suggestions: List<AddressAutofillSuggestion>) {}
            override fun onError(e: Exception) {}
        })

        /* when is received a click on the search bar, the search results view is shown */
        searchLocation.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                if (ignoreNextQueryTextUpdate) {
                    ignoreNextQueryTextUpdate = false
                    return
                }

                val query = Query.create(text.toString())
                if (query != null) {
                    lifecycleScope.launchWhenStarted {
                        // ALERTA: AddressAutoFill
                        Log.i("AddressAutofill SEARCH QUERY", "Searching for: $query")
                        addressAutofillUiAdapter.search(query) // this function is used to search the address
                    }
                }
                searchResultsView.isVisible = query != null
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Nothing to do
            }

            override fun afterTextChanged(s: Editable) {
                // Nothing to do
            }
        })

        // event: when the map is moved, flag for user interaction is set to true
        mapView.mapboxMap.addOnCameraChangeListener {
            isUserInteracting = true
        }
        // event: when the map is idle, the center of the map is obtained
        mapView.mapboxMap.addOnMapIdleListener {
            if (!isUserInteracting) { // if the map is being moved by the user, the center is not obtained
                return@addOnMapIdleListener
            }

            // the map is stopped and obtain the center
            val center = mapView.mapboxMap.cameraState.center
            Log.i("Center", center.toString())

            // ALERTA: solo comentar para pruebas seguras y especificas, ya que consume mucho
            // cada vez que se mueve el mapa, se obtiene la dirección del centro
            // es un costo adicional
            obtainNamePlace(center.longitude(), center.latitude())

            isUserInteracting = false
        }


        locateCurrentPosition()
        obtainAllLocations()
        setupMap()
        navigationMenu()
    }

    private fun sharedGlobalCoordinates() {
        val sharedPref = getSharedPreferences("GlobalPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("currentLocation", txtCurrentLocation.text.toString())
            putString("latitude", coordinatesCurrentLocation.latitude().toString())
            putString("longitude", coordinatesCurrentLocation.longitude().toString())
            apply()
        }
    }

    private fun findAddress(point: Point) {
        lifecycleScope.launchWhenStarted {
            if(contCallAutoFill % 100 != 0) return@launchWhenStarted // this for not to consume request innecesary

            val response = addressAutofill.reverse(point, AddressAutofillOptions())
            response.onValue { suggestions ->
                Log.i("AddressAutofill FINDADDRESS", suggestions[0].toString())
                if (suggestions.isNotEmpty()) {
                    txtCurrentLocation.text = suggestions[0].suggestion.name
                    coordinatesCurrentLocation = suggestions[0].suggestion.coordinate!!

                    val sharedPref = getSharedPreferences("GlobalPrefs", MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("currentLocation", txtCurrentLocation.text.toString())
                        putString("latitude", coordinatesCurrentLocation.latitude().toString())
                        putString("longitude", coordinatesCurrentLocation.longitude().toString())
                        apply()
                    }

                    Log.i("Suggestions", suggestions.toString())
                }
            }.onError {
                //showToast(R.string.address_autofill_error_pin_correction)
            }

            contCallAutoFill++;
        }
    }

    // function to select the suggestion
    private fun selectSuggestion(suggestion: AddressAutofillSuggestion) {
        lifecycleScope.launchWhenStarted {
            // ALERTA AddressAutoFill
            val response = addressAutofill.select(suggestion)
            response.onValue { result ->
                Log.i("AddressAutofill SELECTSUGGESTION", "Selected suggestion: $result")
                // obtaining the point of the selected location
                coordinatesCurrentLocation = result.suggestion.coordinate!!

                sharedGlobalCoordinates()
                // showing the result.address
                showAddressAutofillResult(result)
            }.onError {
                Log.e("AddressAutofill", "Error selecting suggestion: $it")
            }
        }
    }

    // function to show the address in the map
    private fun showAddressAutofillResult(result: AddressAutofillResult) {
        val address = result.address

        Log.i("AddressAutofill showAddressAutofillResult", "Address: $address")

        // move the camera to the selected location
        moveCamera(coordinatesCurrentLocation.longitude(), coordinatesCurrentLocation.latitude())
        ignoreNextMapIdleEvent = true
        ignoreNextQueryTextUpdate = true

        // set the name of the current location in the text view
        txtCurrentLocation.text = listOfNotNull(
            address.houseNumber,
            address.street
        ).joinToString()
        searchLocation.clearFocus()

        // clear the edit text
        searchLocation.text.clear()
        searchResultsView.isVisible = false
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
                    intent.putExtra("token", token)
                    startActivity(intent)
                    true
                }
                R.id.nav_report -> {
                    val intent = Intent(this, ListReportsActivity::class.java)
                    intent.putExtra("token", token)
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
        val service = RetrofitClient.getClient(token)
        val reportsLocations = HashMap<Int, String>()
        service.getAllReports().enqueue(object: Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val reports = response.body()
                if (reports != null) {
                    for(report in reports) {
                        reportsLocations[report.id] = report.type
                    }
                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Log.e("Error MAP", t.message.toString())
            }
        })

        service.getLocations().enqueue(object: Callback<List<Beans.Location>> {
            override fun onResponse(call: Call<List<Beans.Location>>, response: Response<List<Beans.Location>>) {
                val locations = response.body()
                if (locations != null) {
                    for (location in locations) {
                        if(location.alatitude == 0.0 && location.alongitude == 0.0) continue

                        var typeReport = R.drawable.alert_marker
                        when (reportsLocations[location.idReport]) {
                            "Robo" -> typeReport = R.drawable.alert_marker
                            "Accidente" -> typeReport = R.drawable.accident_marker
                            "Falta de iluminación" -> typeReport = R.drawable.illumination_marker
                            "Acoso" -> typeReport = R.drawable.acoso_marker
                            "Otro" -> typeReport = R.drawable.other_marker
                        }

                        addMarker(location.alatitude, location.alongitude, typeReport)
                    }
                }
            }

            override fun onFailure(call: Call<List<Beans.Location>>, t: Throwable) {
                Log.e("Error MAP", t.message.toString())
            }
        })

    }

    private fun addMarker(latitude: Double, longitude: Double, svgResId: Int) {
        val drawable = AppCompatResources.getDrawable(this, svgResId)
        val bitmap = drawableToBitmap(drawable!!)

        val point = Point.fromLngLat(longitude, latitude)
        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconSize(0.5)
        pointAnnotationManager.create(pointAnnotationOptions)
    }
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
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
        locationComponentPlugin.updateSettings {
            enabled = true
            locationPuck = LocationPuck2D(
                topImage = null,
                bearingImage = null,
                shadowImage = null
            )
        }

        locationComponentPlugin.addOnIndicatorPositionChangedListener{ point ->
            val latitude = point.latitude()
            val longitude = point.longitude()

            if (c == 0) {
                obtainNamePlace(longitude, latitude)
                moveCamera(longitude, latitude)
                sharedGlobalCoordinates()
                c++
            }
        }
    }

    private fun obtainNamePlace(longitude: Double, latitude: Double) {
        // obtaining the name of the current location
        val service = RetrofitClientMapbox.getClient()
        Log.i("Geocoding API obtainNamePlace", "Longitude: $longitude, Latitude: $latitude")
        service.getPlace(longitude, latitude, getString(R.string.mapbox_access_token)).enqueue(object: Callback<PropertiesPlace> {
            override fun onResponse(call: Call<PropertiesPlace>, response: Response<PropertiesPlace>) {
                val place = response.body()
                Log.i("Place", place.toString())

                // set the name of the current location in the text view
                currentLocation = place?.features?.get(0)?.properties?.name_preferred.toString()
                coordinatesCurrentLocation = Point.fromLngLat(longitude, latitude)
                txtCurrentLocation.text = currentLocation

                sharedGlobalCoordinates()
            }

            override fun onFailure(call: Call<PropertiesPlace>, t: Throwable) {
                Log.e("Error MAP", t.message.toString())
            }
        })
    }

    private fun moveCamera(longitude: Double, latitude: Double) {
        mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(15.0)
                .build()
        )
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