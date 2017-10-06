package com.vts.cameramonitor.entity

import java.sql.Timestamp
import java.time.LocalDateTime

class ImageEntity {
    constructor(id:Int, name:String, createTime: Timestamp) {
        this.id = id;
        this.name = name;
        this.createTime = createTime.toLocalDateTime().toString();
    }
    constructor(id:Int,name: String,primaryImage: ByteArray) {
        this.id = id;
        this.name = name;
        this.primaryImage = primaryImage;
    }
    constructor(name:String, primaryImage:ByteArray, createUser:String, createTime:LocalDateTime = LocalDateTime.now()) {
        this.name = name;
        this.primaryImage = primaryImage;
        this.createUser = createUser;
        this.createTime = createTime.toString();
        if(this.modifyUser == "") {
            this.modifyUser = createUser;
        }
        this.modifyTime = createTime.toString();
    }
    constructor(id:Int, detectImage: ByteArray, detectResult:String, detectUser:String, detectTime:LocalDateTime= LocalDateTime.now()) {
        this.id = id;
        this.detectImage = detectImage;
        this.detectResult = detectResult;
        this.modifyUser = detectUser;
        this.detectUser = detectUser;
        this.detectTime = detectTime.toString();
    }
    constructor(id:Int, name:String, detectResult: String, detectTime: Timestamp) {
        this.id = id;
        this.name = name;
        this.detectResult = detectResult;
        this.detectTime = detectTime.toLocalDateTime().toString();
    }
    constructor(id:Int, name:String, primaryImage:ByteArray, detectImage:ByteArray, detectResult: String, createUser:String, createTime:Timestamp, modifyUser:String, modifyTime: Timestamp, detectUser: String, detectTime: Timestamp) {
        this.id = id;
        this.name = name;
        this.primaryImage = primaryImage;
        this.detectImage = detectImage;
        this.detectResult = detectResult;
        this.createUser = createUser;
        this.createTime = createTime.toLocalDateTime().toString();
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime.toLocalDateTime().toString();
        this.detectUser = detectUser;
        this.detectTime = detectTime.toLocalDateTime().toString();
    }
    var id:Int = -1;
    var name:String = "";
    var primaryImage:ByteArray = ByteArray(0);
    var detectImage:ByteArray = ByteArray(0);
    var createUser:String = "";
    var createTime:String = LocalDateTime.now().toString();
    var modifyUser:String = "";
    var modifyTime:String = LocalDateTime.now().toString();
    var detectResult:String? = null;
    var detectUser:String? = null;
    var detectTime:String? = null;
}