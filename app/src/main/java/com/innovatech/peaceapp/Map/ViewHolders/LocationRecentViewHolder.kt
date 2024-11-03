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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationRecentViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val addressLocation = view.findViewById<TextView>(R.id.txtAddressRecentLocation)
    val secondAddressLocation = view.findViewById<TextView>(R.id.txtSecAddressRecentLocation)
    var firstAddress = ""
    var secondAddress = ""

    fun render(location: LocationModel) {
        obtainNamePlace(location.longitude!!, location.latitude!!)
    }

    private fun obtainNamePlace(longitude: Double, latitude: Double) {
        // obtaining the name of the current location
        val service = RetrofitClientMapbox.getClient()
        Log.i("Geocoding API obtainNamePlace", "Longitude: $longitude, Latitude: $latitude")
        service.getPlace(longitude, latitude, itemView.context.getString(R.string.mapbox_access_token)).enqueue(object :
            Callback<PropertiesPlace> {
            override fun onResponse(call: Call<PropertiesPlace>, response: Response<PropertiesPlace>) {
                val place = response.body()
                Log.i("andriushhh", place.toString())

                firstAddress = place?.features?.get(0)?.properties?.name_preferred.toString()
                secondAddress = place?.features?.get(0)?.properties?.full_address.toString()

                addressLocation.text = firstAddress
                secondAddressLocation.text = secondAddress
            }

            override fun onFailure(call: Call<PropertiesPlace>, t: Throwable) {
                Log.e("Error MAP", t.message.toString())
            }
        })
    }


}