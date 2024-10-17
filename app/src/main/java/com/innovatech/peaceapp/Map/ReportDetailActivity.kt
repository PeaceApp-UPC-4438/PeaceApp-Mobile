package com.innovatech.peaceapp.Map

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.GlobalToken
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.R
import com.squareup.picasso.Picasso

class ReportDetailActivity : AppCompatActivity() {
    lateinit var token: String
    private lateinit var report: Report
    private lateinit var txtTipoReporte: TextView
    private lateinit var imgReporte: ImageView
    private lateinit var txtUbicacionReporte: TextView
    private lateinit var txtFechaReporte: TextView
    private lateinit var txtDescripcionReporte: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        token = GlobalToken.token
        report = intent.getSerializableExtra("report") as Report
        txtTipoReporte = findViewById(R.id.txtTipoReporte)
        imgReporte = findViewById(R.id.imgReporte)
        txtUbicacionReporte = findViewById(R.id.txtUbicacionReporte)
        txtFechaReporte = findViewById(R.id.txtFechaReporte)
        txtDescripcionReporte = findViewById(R.id.txtDescripcionReporte)
        setReportData()
        navigationMenu()
    }

    private fun setReportData() {
        txtTipoReporte.text = report.type
        txtUbicacionReporte.text = report.address
        txtFechaReporte.text = report.createdAt
        txtDescripcionReporte.text = report.detail
        Picasso.get().load(report.image)
            .resize(300, 300)
            .centerCrop().into(imgReporte)
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
}