package com.innovatech.peaceapp.Map

import Beans.Location
import Beans.LocationSchema
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.Map.Beans.ReportSchema
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewReportActivity : AppCompatActivity() {
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_report)
        token = intent.getStringExtra("token")!!

        val typeReport = intent.getStringExtra("type")
        // to show it in the console
        Log.d("TypeReport", typeReport.toString())
        val txtTypeReport = findViewById<TextView>(R.id.txtTypeReport)
        txtTypeReport.text = typeReport.toString()
        val edtLatitude = findViewById<EditText>(R.id.edtLatitude)
        val edtLongitude = findViewById<EditText>(R.id.edtLongitude)


        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            val intent = Intent(this, ListReportsActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val edtTitle = findViewById<EditText>(R.id.edtTitle)
            val edtDetail = findViewById<EditText>(R.id.edtDetail)


            val title = edtTitle.text.toString()
            val detail = edtDetail.text.toString()
            val latitude = edtLatitude.text.toString()
            val longitude = edtLongitude.text.toString()

            if (title.isEmpty() || detail.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
                return@setOnClickListener
            }

            saveReport(title, detail, latitude, longitude, typeReport.toString())


        }

        navigationMenu()
    }

    private fun navigationMenu() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
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

        bottomNavigationView.menu.findItem(R.id.nav_report).setChecked(true)
    }


    private fun saveReport(title: String, detail: String, latitude: String, longitude: String, typeReport: String) {
        val service = RetrofitClient.getClient(token)

        val report = ReportSchema(title, detail, typeReport, 2, null)
        // obtain the id of the report created

        service.postReport(report).enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {
                if (response.isSuccessful) {
                    val report = response.body()
                    if (report != null) {
                        Log.d("Report", report.toString())
                        saveLocation(latitude, longitude, report.id)
                    }
                }
            }

            override fun onFailure(call: Call<Report>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }

    private fun saveLocation(latitude: String, longitude: String, idReport: Int) {
        val service = RetrofitClient.getClient(token)

        val location = LocationSchema(latitude, longitude, idReport)

        service.postLocation(location).enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                if (response.isSuccessful) {
                    val location = response.body()
                    if (location != null) {
                        Log.d("Location", location.toString())
                    }

                    val intent = Intent(this@NewReportActivity, MapActivity::class.java)
                    intent.putExtra("token", token)

                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }
}