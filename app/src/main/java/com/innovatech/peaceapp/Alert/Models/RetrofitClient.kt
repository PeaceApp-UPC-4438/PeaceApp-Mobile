package com.innovatech.peaceapp.Alert.Models

import com.innovatech.peaceapp.Alert.Interfaces.PlaceHolder
import com.innovatech.peaceapp.Map.Interfaces.PlaceHolderMapbox
import com.innovatech.peaceapp.Map.Models.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://10.0.2.2:8080/" // Cambia la URL si es necesario

    // Función para obtener el cliente con autenticación para alertas
    fun getClient(token: String): PlaceHolder {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token)) // Agrega el token al cliente
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Usa el cliente OkHttp con el interceptor para el token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceHolder::class.java) // Crea el cliente con la interfaz de alertas
    }
}
object RetrofitClientMapbox {
    const val BASE_URL = "https://api.mapbox.com/search/geocode/v6/"

    fun getClient(): PlaceHolderMapbox {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceHolderMapbox::class.java)
    }
}
