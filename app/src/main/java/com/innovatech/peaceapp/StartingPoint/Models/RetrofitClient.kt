package com.innovatech.peaceapp.StartingPoint.Models

import com.innovatech.peaceapp.StartingPoint.Interface.PlaceHolder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.45:8080/"

    val placeHolder: PlaceHolder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceHolder::class.java)
}