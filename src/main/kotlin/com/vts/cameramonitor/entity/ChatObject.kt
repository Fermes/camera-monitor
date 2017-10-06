package com.vts.cameramonitor.entity

class ChatObject {

    var userName: String = ""
    var message: String = ""

    constructor() {}

    constructor(userName: String, message: String) : super() {
        this.userName = userName
        this.message = message
    }
}