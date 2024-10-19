package com.innovatech.peaceapp.Alert.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.Alert.Beans.Alert
import com.innovatech.peaceapp.R
import com.squareup.picasso.Picasso

class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val alertImg = view.findViewById<ImageView>(R.id.imgAlert)  // Assuming there's an image
    val alertType = view.findViewById<TextView>(R.id.txtType)
    val alertLocation = view.findViewById<TextView>(R.id.txtLocation)
    val alertDescription = view.findViewById<TextView>(R.id.txtDescription)

    fun render(alertModel: Alert) {
        alertType.text = alertModel.type  // Set the type of alert
        alertLocation.text = alertModel.location  // Set the location of the alert
        alertDescription.text = alertModel.description ?: "No description provided"  // Set the description or a default text

        // Optionally load an image, for now, just using a placeholder
        Picasso.get().load(R.drawable.image_report_not_found)  // If there's no image URL, use a placeholder
            .resize(300, 300)
            .centerCrop().into(alertImg)
    }
}
