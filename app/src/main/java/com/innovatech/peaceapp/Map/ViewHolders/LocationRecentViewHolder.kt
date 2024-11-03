package com.innovatech.peaceapp.Map.ViewHolders

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.DB.AppDatabase
import com.innovatech.peaceapp.DB.Entities.LocationModel
import com.innovatech.peaceapp.Map.Beans.PropertiesPlace
import com.innovatech.peaceapp.Map.Models.RetrofitClientMapbox
import com.innovatech.peaceapp.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.camera
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationRecentViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val addressLocation = view.findViewById<TextView>(R.id.txtAddressRecentLocation)
    val secondAddressLocation = view.findViewById<TextView>(R.id.txtSecAddressRecentLocation)

    fun render(location: LocationModel) {
        addressLocation.text = location.firstAddress
        secondAddressLocation.text = location.secondAddress

        /*
        itemView.setOnClickListener {
            moveCamera(location.longitude!!, location.latitude!!)
        }*/
    }

    private fun moveCamera(longitude: Double, latitude: Double) {
        val mapView = R.id.mapView as MapView
        mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(15.0)
                .build()
        )
    }

}