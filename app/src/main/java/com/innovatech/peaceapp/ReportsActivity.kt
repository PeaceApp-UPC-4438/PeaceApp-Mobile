package com.innovatech.peaceapp

import Beans.Location
import Beans.LocationSchema
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reports)

        val latitudeInput: EditText = findViewById(R.id.latitude)
        val longitudeInput: EditText = findViewById(R.id.longitude)
        val idReportInput: EditText = findViewById(R.id.idReport)
        val submitButton: Button = findViewById(R.id.save)

        submitButton.setOnClickListener {
            val location = LocationSchema(
                latitudeInput.text.toString(),
                longitudeInput.text.toString(),
                idReportInput.text.toString().toInt()
            )

            val service = RetrofitClient.placeHolder
            service.postLocation(location).enqueue(object: Callback<Location> {
                override fun onResponse(call: Call<Location>, response: Response<Location>) {
                    Log.i("ANDROIS POST", response.body().toString())

                    Log.i("POST", "Location posted")
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.e("POST", "Location not posted")
                }
            })

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}