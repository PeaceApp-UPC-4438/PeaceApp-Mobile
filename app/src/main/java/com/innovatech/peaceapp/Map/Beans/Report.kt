package com.innovatech.peaceapp.Map.Beans

data class Report(
    var id: Int,
    var createdAt: String,
    var updatedAt: String,
    var idUser: Int,
    var detail: String,
    var title: String,
    var type: String,
    var image: String?
)