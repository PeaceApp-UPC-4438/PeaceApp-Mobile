package com.innovatech.peaceapp.Map.Models
import com.innovatech.peaceapp.Map.Interfaces.PlaceHolder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.45:8080/"

    fun getClient(token: String): PlaceHolder {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token)) // Agrega el token
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Usa el OkHttpClient que incluye el token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlaceHolder::class.java)
    }
}
