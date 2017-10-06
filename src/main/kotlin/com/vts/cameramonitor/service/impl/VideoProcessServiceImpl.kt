package com.vts.cameramonitor.service.impl

import com.vts.cameramonitor.service.VideoProcessService
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VideoProcessServiceImpl: VideoProcessService, Thread(){
    override var video = Any();
    override var isStop = false;
    override var isOpened = false;
    override var imageByteArray = ByteArray(0);
    override var saveInterval = 10;
    override var videoAddress = "";
    /*init {
        val tmpImg = Imgcodecs.imread("video-big.png");
        val matOfByte = MatOfByte();
        Imgcodecs.imencode(".png",tmpImg,matOfByte);
        imageByteArray = matOfByte.toArray();
    }*/
    override fun run(){
        if(!isOpened) {
            println("打开视频失败");
            return;
        }
        val logger = LoggerFactory.getLogger(VideoProcessServiceImpl::class.qualifiedName);
        var timer = 0;
        while(!isStop) {
            val tmpImg = Mat();
            try {
                (video as VideoCapture).read(tmpImg);
                Imgproc.resize(tmpImg, tmpImg, Size(640.0,360.0));
                val matOfByte = MatOfByte();
                Imgcodecs.imencode(".jpg",tmpImg,matOfByte);
                imageByteArray = matOfByte.toArray();
            } catch (e:Exception) {

            } finally {
                tmpImg.release();
            }
            Thread.sleep(20);
            timer = (timer + 1) % 101;
            if (timer == 100) {
                println("------video------");
            }
            //println("1024");
        }
        println(videoAddress + "  已停止");
    }

    override fun openVideo(videoAddress: String):Boolean{
        video = VideoCapture(videoAddress);
        if(!(video as VideoCapture).isOpened) {
            println("打开视频失败");
            return false;
        }
        this.videoAddress = videoAddress;
        isOpened = true;
        isStop = false;
        return true;
    }
}