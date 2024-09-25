package com.innovatech.peaceapp.Map

import Beans.Location
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innovatech.peaceapp.Map.Adapters.AdapterReport
import com.innovatech.peaceapp.Map.Beans.Report
import com.innovatech.peaceapp.Map.Models.RetrofitClient
import com.innovatech.peaceapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_reports)

        val allReports: LinearLayout = findViewById(R.id.allReports)
        val myReports: LinearLayout = findViewById(R.id.myReports)
        val txtAllReports: TextView = findViewById(R.id.txtAllReports)
        val txtMyReports: TextView = findViewById(R.id.txtMyReports)
        val btnNewReport: Button = findViewById(R.id.btnNewReport)

        tabSelected(myReports, txtMyReports)
        tabUnselected(allReports, txtAllReports)


        // Todos los reportes is selected
        allReports.setOnClickListener {
            tabSelected(myReports, txtMyReports)
            tabUnselected(allReports, txtAllReports)
            obtainAllReports()
        }

        // Mis reportes is selected
        myReports.setOnClickListener {
            tabSelected(allReports, txtAllReports)
            tabUnselected(myReports, txtMyReports)
            obtainMyReports()
        }

        btnNewReport.setOnClickListener {
            val intent = Intent(this, TypeReportsActivity::class.java)
            startActivity(intent)
        }

        obtainAllReports()
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
                    startActivity(intent)
                    true
                }
                R.id.nav_report -> {
                    val intent = Intent(this, ListReportsActivity::class.java)
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


    private fun obtainAllReports() {
        val service = RetrofitClient.placeHolder
        service.getAllReports().enqueue(object: Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val reports = response.body()
                val listReports = mutableListOf<Report>()

                if(reports != null) {
                    for(report in reports) {

                        if(report.image == null) report.image = "https://as1.ftcdn.net/v2/jpg/04/62/93/66/1000_F_462936689_BpEEcxfgMuYPfTaIAOC1tCDurmsno7Sp.jpg"

                        listReports.add(
                            Report(
                                report.id,
                                report.createdAt,
                                report.updatedAt,
                                report.idUser,
                                report.detail,
                                report.title,
                                report.type,
                                report.image
                            )
                        )
                    }

                    val recycler = findViewById<RecyclerView>(R.id.recyclerReports)
                    recycler.layoutManager = LinearLayoutManager(applicationContext)
                    recycler.adapter = AdapterReport(listReports)
                }
            }

            override fun onFailure(p0: Call<List<Report>>, p1: Throwable) {
                Log.e("GET REPORTS", "Reports not obtained")
            }
        })
    }

    private fun obtainMyReports() {
        val service = RetrofitClient.placeHolder
        val userId = 2; // here will go the user id ANDIRUUUUUUUSHINI
        service.getMyReports(userId).enqueue(object: Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                val reports = response.body()
                val listReports = mutableListOf<Report>()

                if(reports != null) {
                    for(report in reports) {

                        if(report.image == null) report.image = "https://as1.ftcdn.net/v2/jpg/04/62/93/66/1000_F_462936689_BpEEcxfgMuYPfTaIAOC1tCDurmsno7Sp.jpg"

                        listReports.add(
                            Report(
                                report.id,
                                report.createdAt,
                                report.updatedAt,
                                report.idUser,
                                report.detail,
                                report.title,
                                report.type,
                                report.image
                            )
                        )
                    }

                    val recycler = findViewById<RecyclerView>(R.id.recyclerReports)
                    recycler.layoutManager = LinearLayoutManager(applicationContext)
                    recycler.adapter = AdapterReport(listReports)
                }
            }

            override fun onFailure(p0: Call<List<Report>>, p1: Throwable) {
                Log.e("GET REPORTS", "Reports not obtained")
            }
        })
    }

    // Function to change the background of the tab when it is UNSELECTED
    private fun tabSelected(tab: LinearLayout, txt: TextView) {
        tab.setBackgroundResource(R.drawable.tab_selected)
        txt.setTextColor(resources.getColor(R.color.peaceapp_blue_dark_disable))
    }

    // Function to change the background of the tab when it is SELECTED
    private fun tabUnselected(tab: LinearLayout, txt: TextView) {
        tab.setBackgroundResource(R.drawable.rounded_top_corners_white_map)
        txt.setTextColor(resources.getColor(R.color.black))
    }

    private fun cleanReports() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerReports)
        recycler.adapter = null
    }
}