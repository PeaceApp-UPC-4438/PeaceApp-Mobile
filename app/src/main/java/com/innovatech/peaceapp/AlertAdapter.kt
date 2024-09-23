package com.innovatech.peaceapp

import Beans.Alert
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlertAdapter(private val alertList: List<Alert>) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {

    // ViewHolder para cada elemento en la lista
    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.txtAlertTitle)
        val location: TextView = itemView.findViewById(R.id.txtAlertLocation)
        val time: TextView = itemView.findViewById(R.id.txtAlertTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_card, parent, false)
        return AlertViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val currentAlert = alertList[position]
        holder.title.text = currentAlert.title
        holder.location.text = currentAlert.location
        holder.time.text = currentAlert.time
    }

    override fun getItemCount(): Int {
        return alertList.size
    }
}
