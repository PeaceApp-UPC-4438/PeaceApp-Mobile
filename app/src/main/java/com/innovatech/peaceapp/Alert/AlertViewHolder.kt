package com.innovatech.peaceapp.Alert

import com.innovatech.peaceapp.Alert.Beans.Alert
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.R

class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // Referencias a los TextViews de la tarjeta de alerta
    val alertTitle: TextView = view.findViewById(R.id.txtAlertTitle)
    val alertLocation: TextView = view.findViewById(R.id.txtAlertLocation)
    val alertTime: TextView = view.findViewById(R.id.txtAlertTime)

    // Método para llenar los datos de una alerta
    fun render(alertModel: Alert) {
        alertTitle.text = alertModel.title
        alertLocation.text = alertModel.location
        alertTime.text = alertModel.time
    }
}
