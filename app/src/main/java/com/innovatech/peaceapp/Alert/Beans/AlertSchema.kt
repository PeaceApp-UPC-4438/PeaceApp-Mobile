package com.innovatech.peaceapp.Alert.Beans

data class AlertSchema(
    var location: String,
    var type: String,
    var description: String?, // Optional field
    var idUser: Int,
    var image_url: String? // New field for image URL
)
