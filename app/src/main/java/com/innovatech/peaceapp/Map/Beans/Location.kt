package Beans

class Location {
    var id: Int
    var createdAt: String
    var updatedAt: String
    var idReport: Int
    var alongitude: Double
    var alatitude: Double

    constructor(id: Int, createdAt: String, updatedAt: String, idReport: Int, longitude: String, latitude: String) {
        this.id = id
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        this.idReport = idReport
        this.alongitude = longitude.toDouble()
        this.alatitude = latitude.toDouble()
    }

    constructor(latitude: String,  longitude: String, idReport: String) {
        this.alatitude = latitude.toDouble()
        this.alongitude = longitude.toDouble()
        this.idReport = idReport.toInt()
        this.id = 0
        this.createdAt = ""
        this.updatedAt = ""
    }
}