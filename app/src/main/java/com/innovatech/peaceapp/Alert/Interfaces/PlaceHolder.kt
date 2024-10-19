package com.innovatech.peaceapp.Alert.Interfaces

import Beans.Location
import com.innovatech.peaceapp.Alert.Beans.Alert
import com.innovatech.peaceapp.Alert.Beans.AlertSchema
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlaceHolder {
    // POST api/v1/alerts/
    // body: {"location": "location", "type": "type", "description": "optional description", "idUser": 1}
    @POST("api/v1/alerts/")
    fun postAlert(@Body alertSchema: AlertSchema): Call<Alert>

    // GET api/v1/alerts/{id}
    @GET("api/v1/alerts/{id}")
    fun getAlertById(@Path("id") id: Int): Call<Alert>

    // GET api/v1/alerts/user/{userId}
    @GET("api/v1/alerts/user/{userId}")
    fun getAlertsByUser(@Path("userId") userId: Int): Call<List<Alert>>

    // Location endpoints
    // GET api/v1/locations/dangerous
    @GET("api/v1/locations/dangerous")
    fun getDangerousLocations(): Call<List<Location>>

    // GET api/v1/locations/
    @GET("api/v1/locations/")
    fun getAllLocations(): Call<List<Location>>
}
