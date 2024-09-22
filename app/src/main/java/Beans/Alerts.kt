package Beans

class Alert {
    var id:Int = 0  // Default value for id
    var title:String
    var location:String
    var time:String
    constructor(id: Int,title: String, location: String, time: String){
        this.id=id
        this.title=title
        this.location=location
        this.time=time
    }
    constructor(title: String, location: String, time: String){
        this.title=title
        this.location=location
        this.time=time
    }
}