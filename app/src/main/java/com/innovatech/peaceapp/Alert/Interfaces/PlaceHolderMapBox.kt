package com.innovatech.peaceapp.Alert.Interfaces

import com.innovatech.peaceapp.Map.Beans.PropertiesPlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceHolderMapbox {
    // Endpoint para obtener información de una ubicación basada en latitud y longitud (reverse geocoding)
    @GET("reverse")
    fun getPlace(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("access_token") access_token: String
    ): Call<PropertiesPlace>

    // Endpoint para obtener ubicaciones peligrosas desde Mapbox (si las tienes en Mapbox)
    @GET("mapbox/dangerous-locations")
    fun getDangerousLocations(
        @Query("access_token") access_token: String
    ): Call<List<PropertiesPlace>>
}