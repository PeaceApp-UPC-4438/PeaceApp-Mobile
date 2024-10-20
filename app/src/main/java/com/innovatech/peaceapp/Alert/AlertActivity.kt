package com.innovatech.peaceapp.Alert

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.Alert.Adapters.AlertAdapter
import com.innovatech.peaceapp.Alert.Beans.Alert
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.Alert.Models.RetrofitClient
import com.innovatech.peaceapp.GlobalToken.token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlertActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtCurrentLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_list)

        // Initialize views
        txtCurrentLocation = findViewById(R.id.tvCurrentLocation)
        recyclerView = findViewById(R.id.recyclerAlert)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get current location from intent
        val currentLocation = intent.getStringExtra("currentLocation") ?: "Location not available"
        Log.i("AlertActivity", "Current Location Received: $currentLocation") // Log for debugging
        txtCurrentLocation.text = currentLocation // Set current location in TextView

        // Fetch and display alerts
        fetchAlerts()
    }

    private fun fetchAlerts() {
        val api = RetrofitClient.getClient(token)

        // Make the API call to fetch alerts
        api.getAllAlerts().enqueue(object : Callback<List<Alert>> {
            override fun onResponse(call: Call<List<Alert>>, response: Response<List<Alert>>) {
                if (response.isSuccessful) {
                    val alerts = response.body()
                    Log.d("AlertActivity", "Fetched alerts: $alerts") // Log to confirm data
                    if (alerts != null) {
                        setupRecyclerView(alerts)
                    } else {
                        txtCurrentLocation.text = "No alerts available"
                    }
                } else {
                    txtCurrentLocation.text = "Failed to load alerts"
                }
            }

            override fun onFailure(call: Call<List<Alert>>, t: Throwable) {
                txtCurrentLocation.text = "Error: ${t.message}"
            }
        })
    }

    private fun setupRecyclerView(alerts: List<Alert>) {
        recyclerView.adapter = AlertAdapter(alerts)
    }
}
