package com.vts.cameramonitor.service

import org.opencv.videoio.VideoCapture

interface VideoProcessService {
    /**
     * video:OpenCV-VideoCapture
     */
    var video:Any;

    /**
     * isStop:决定是否结束进程
     */
    var isStop:Boolean;

    var isOpened:Boolean;
    var imageByteArray:ByteArray;
    var saveInterval:Int;
    var videoAddress:String;
    fun run();
    fun openVideo(videoAddress:String):Boolean;
}