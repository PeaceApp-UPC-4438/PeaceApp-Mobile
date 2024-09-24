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
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.Map.Beans.ReportSchema
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_report)

        val typeReport = intent.getStringExtra("type")
        // to show it in the console
        Log.d("TypeReport", typeReport.toString())
        val txtTypeReport = findViewById<TextView>(R.id.txtTypeReport)
        txtTypeReport.text = typeReport.toString()

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val edtTitle = findViewById<EditText>(R.id.edtTitle)
            val edtDetail = findViewById<EditText>(R.id.edtDetail)
            val edtLatitude = findViewById<EditText>(R.id.edtLatitude)
            val edtLongitude = findViewById<EditText>(R.id.edtLongitude)

            val title = edtTitle.text.toString()
            val detail = edtDetail.text.toString()
            val latitude = edtLatitude.text.toString()
            val longitude = edtLongitude.text.toString()

            if (title.isEmpty() || detail.isEmpty() || latitude.isEmpty() || longitude.isEmpty()) {
                return@setOnClickListener
            }

            saveReport(title, detail, latitude, longitude, typeReport.toString())


        }

    }

    private fun saveReport(title: String, detail: String, latitude: String, longitude: String, typeReport: String) {
        val service = RetrofitClient.placeHolder

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
        val service = RetrofitClient.placeHolder

        val location = LocationSchema(latitude, longitude, idReport)

        service.postLocation(location).enqueue(object : Callback<Location> {
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                if (response.isSuccessful) {
                    val location = response.body()
                    if (location != null) {
                        Log.d("Location", location.toString())
                    }

                    val intent = Intent(this@NewReportActivity, MapActivity::class.java)

                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }
        })
    }
}