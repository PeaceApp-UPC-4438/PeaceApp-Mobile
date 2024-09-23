package com.innovatech.peaceapp

import Beans.Alert
import Persistence.OpenHelper
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlertActivity : AppCompatActivity() {

    lateinit var dbHelper: OpenHelper
    lateinit var recycler: RecyclerView
    lateinit var alertAdapter: AlertAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.alert_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the RecyclerView for displaying alerts
        recycler = findViewById(R.id.recyclerAlert)
        recycler.layoutManager = LinearLayoutManager(applicationContext)

        // Initialize the database helper
        dbHelper = OpenHelper(this)

        // Automatically add a new alert when the app is opened
        addNewAlertAutomatically()

        // Load and display all alerts from the database
        getAllAlerts()
    }

    // Function to automatically add an alert
    private fun addNewAlertAutomatically() {
        val newAlert = Alert(
            title = "Alerta Automática",
            location = "Ubicación Automática",
            time = "12:00 p.m."
        )

        // Save the new alert to the database
        dbHelper.nuevaAlerta(newAlert)
    }

    // Function to load all alerts from the database and display them
    private fun getAllAlerts() {
        val data = dbHelper.leerAlertas()  // Fetch all alerts from the database
        alertAdapter = AlertAdapter(data)  // Create an adapter for the alert data
        recycler.adapter = alertAdapter    // Set the adapter to the RecyclerView
    }
}
