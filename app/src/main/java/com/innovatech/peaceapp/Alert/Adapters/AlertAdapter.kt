package com.innovatech.peaceapp.Alert.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.Alert.Beans.Alert
import com.innovatech.peaceapp.Alert.ViewHolders.AlertViewHolder
import com.innovatech.peaceapp.R

class AdapterAlert(val alertsList: List<Alert>) : RecyclerView.Adapter<AlertViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AlertViewHolder(layoutInflater.inflate(R.layout.alert_card, parent, false))
    }

    override fun getItemCount(): Int = alertsList.size

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val item = alertsList[position]
        holder.render(item)
    }
}
