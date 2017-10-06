package com.vts.cameramonitor.entity

class ImageSQLEntity{
    constructor();
    constructor(imageByte:ByteArray){
        this.imageByte = imageByte;
    }
    var imageByte = ByteArray(0);
}