package com.innovatech.peaceapp.Profile.Beans

class UserProfile {
    var id: Int
    var name: String
    var lastname: String
    var phone: String
    var email: String
    var password: String

    constructor(id: Int, name: String, lastname: String, phone: String, email: String, password: String) {
        this.id = id
        this.name = name
        this.lastname = lastname
        this.phone = phone
        this.email = email
        this.password = password
    }
    constructor(name: String, lastname: String, phone: String, email: String, password: String) {
        this.id = 0
        this.name = name
        this.lastname = lastname
        this.phone = phone
        this.email = email
        this.password = password
    }
}