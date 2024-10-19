package com.innovatech.peaceapp.Alert.Beans

class Alert {
    var id: Int = 0  // Default value for id
    var location: String
    var type: String
    var description: String?
    var idUser: Long

    // Constructor with id (for cases where the Alert is already created)
    constructor(id: Int, location: String, type: String, description: String?, idUser: Long) {
        this.id = id
        this.location = location
        this.type = type
        this.description = description
        this.idUser = idUser
    }

    // Constructor without id (for new alerts being created)
    constructor(location: String, type: String, description: String?, idUser: Long) {
        this.location = location
        this.type = type
        this.description = description
        this.idUser = idUser
    }
}
