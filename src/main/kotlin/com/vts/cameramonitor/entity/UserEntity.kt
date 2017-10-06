package com.vts.cameramonitor.entity

import java.sql.Timestamp
import java.time.LocalDateTime

class UserEntity{
    constructor(username: String, password: String, videoTransform: Int,imageSearch: Int,imageDetect: Int, userManage: Int, createUser: String) {
        this.userName = username;
        this.passWord = password;
        this.videoTransform = videoTransform == 1;
        this.imageSearch = imageSearch == 1;
        this.imageDetect = imageDetect == 1;
        this.userManage = userManage == 1;
        this.createUser = createUser;
    }
    constructor(id:Int,username: String, password: String, videoTransform: Int,imageSearch: Int,imageDetect: Int, userManage: Int){
        this.id = id;
        this.userName = username;
        this.passWord = password;
        this.videoTransform = videoTransform == 1;
        this.imageSearch = imageSearch == 1;
        this.imageDetect = imageDetect == 1;
        this.userManage = userManage == 1;
    }
    constructor(id:Int, username: String, password: String, videoTransform: Int, imageSearch: Int, imageDetect: Int, userManage: Int, createUser:String, createTime:LocalDateTime = LocalDateTime.now(), modifyUser:String = createUser, modifyTime:LocalDateTime = LocalDateTime.now()) : this (username,password,videoTransform,imageSearch,imageDetect,userManage, createUser){
        this.id = id;
        this.createTime = createTime.toString();
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime.toString();
    }
    constructor(id:Int, username: String, password: String, videoTransform: Int, imageSearch: Int, imageDetect: Int, userManage: Int, createUser:String, createTime:Timestamp,  modifyUser:String, modifyTime:Timestamp) : this (username,password,videoTransform,imageSearch,imageDetect,userManage,createUser){
        this.id = id;
        this.createTime = createTime.toLocalDateTime().toString();
        this.modifyUser = modifyUser;
        this.modifyTime = modifyTime.toLocalDateTime().toString();
    }
    var id:Int = -1;
    var userName = "";
    var passWord = "";
    var videoTransform = false;
    var imageSearch = false;
    var imageDetect = false;
    var userManage = false;
    var createUser = "admin";
    var createTime = LocalDateTime.now().toString();
    var modifyUser = "admin";
    var modifyTime = LocalDateTime.now().toString();

}