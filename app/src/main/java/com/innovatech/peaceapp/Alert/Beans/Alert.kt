package com.innovatech.peaceapp.Alert.Beans

class Alert {
    var id: Int = 0  // Default value for id
    var location: String
    var type: String
    var description: String?
    var idUser: Int
    var imageUrl: String? // New property for the image URL

    // Constructor with id (for cases where the Alert is already created)
    constructor(id: Int, location: String, type: String, description: String?, idUser: Int, imageUrl: String?) {
        this.id = id
        this.location = location
        this.type = type
        this.description = description
        this.idUser = idUser
        this.imageUrl = imageUrl // Initialize imageUrl
    }

    // Constructor without id (for new alerts being created)
    constructor(location: String, type: String, description: String?, idUser: Int, imageUrl: String?) {
        this.location = location
        this.type = type
        this.description = description
        this.idUser = idUser
        this.imageUrl = imageUrl // Initialize imageUrl
    }
}
