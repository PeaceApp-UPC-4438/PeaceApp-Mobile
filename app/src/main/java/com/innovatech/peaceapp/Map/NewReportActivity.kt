package com.innovatech.peaceapp.Map

import Beans.Location
import Beans.LocationSchema
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.Map.Beans.ReportSchema
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.R
import com.mapbox.geojson.Point
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class NewReportActivity : AppCompatActivity() {
    lateinit var token: String
    private lateinit var txtTypeReport: TextView
    private lateinit var txtLocation: TextView
    private lateinit var edtTitle: EditText
    private lateinit var edtDetail: EditText
    private lateinit var imgMoreEvidence: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val REQUEST_CODE_IMAGE_PICKER = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_report)
        txtTypeReport = findViewById(R.id.txtTypeReport)
        txtLocation = findViewById(R.id.txtLocation)
        edtTitle = findViewById(R.id.edtTitle)
        edtDetail = findViewById(R.id.edtDetail)
        imgMoreEvidence = findViewById(R.id.imgMoreEvidence)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)


        // recovering the current location coordinates
        val typeReport = intent.getStringExtra("type")
        val sharedPref = getSharedPreferences("GlobalPrefs", MODE_PRIVATE)
        val latitude = sharedPref.getString("latitude", "0.0")!!.toDouble()
        val longitude = sharedPref.getString("longitude", "0.0")!!.toDouble()
        val currentLocation = sharedPref.getString("currentLocation", "No location found")!!

        txtLocation.text = currentLocation
        token = intent.getStringExtra("token")!!

        btnCancel.setOnClickListener {
            val intent = Intent(this, ListReportsActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        btnSave.setOnClickListener {
            val title = edtTitle.text.toString()
            val detail = edtDetail.text.toString()

            if (title.isEmpty() || detail.isEmpty()) {
                return@setOnClickListener
            }
            saveReport(title, detail, latitude, longitude, typeReport.toString())
        }

        saveImageEvidence()
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

    private fun saveReport(title: String, detail: String, latitude: Double, longitude: Double, typeReport: String) {
        val service = RetrofitClient.getClient(token)

        val report = ReportSchema(title, detail, typeReport, 2, null)
        // obtain the id of the report created

        service.postReport(report).enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {
                if (response.isSuccessful) {
                    val report = response.body()
                    if (report != null) {
                        Log.d("Report", report.toString())
                        saveLocation(latitude.toString(), longitude.toString(), report.id)
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

    private fun saveImageEvidence() {
        imgMoreEvidence.setOnClickListener {
            // open the options camera, gallery and file
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            openImageOptions()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                openImageOptions()
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageOptions() {
        val options = arrayOf("Camera", "Gallery", "File")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image From")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> openFilePicker()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                saveImage(imageUri)
            } else {
                val bitmap = data?.extras?.get("data") as Bitmap
                saveImage(bitmap)
            }
        }
    }

    private fun saveImage(imageUri: Uri) {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        saveBitmap(bitmap)
    }

    private fun saveImage(bitmap: Bitmap) {
        saveBitmap(bitmap)
    }

    private fun saveBitmap(bitmap: Bitmap) {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "evidence.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
    }
}