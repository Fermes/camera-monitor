package com.vts.cameramonitor

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
import com.corundumstudio.socketio.AckCallback
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.VoidAckCallback
import com.corundumstudio.socketio.listener.DataListener
import com.vts.cameramonitor.entity.ChatObject
import org.mybatis.spring.annotation.MapperScan
import org.opencv.core.Core
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.File
import java.io.IOException
import java.io.FileOutputStream
import java.io.File.separator

var socketServer:SocketIOServer? = null;
@SpringBootApplication
@MapperScan("com.vts.cameramonitor.mapper")
class CameraMonitorApplication{
    init {
        try {
            val dll = File("opencv_java330.dll");
            println(dll.absolutePath)
            System.load(dll.absolutePath);
            //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            println("OpenCV Loaded");
            dll.deleteOnExit();
        } catch (e: Exception) {
            System.err.println("load jni error!")
        }

        /*
        val config = com.corundumstudio.socketio.Configuration();
        config.hostname = "10.141.5.146";
        config.port = 1001;

        socketServer = SocketIOServer(config);
        socketServer!!.addEventListener("client", ChatObject::class.java, DataListener<ChatObject> { client, data, ackRequest ->
            // check is ack requested by client,
            // but it's not required check
            if (ackRequest.isAckRequested) {
                // send ack response with data to client
                ackRequest.sendAckData("hello", "yeah!")
            }
            println(data);
            // send message back to client with ack callback WITH data
            val ackChatObjectData = ChatObject(data.userName, "message with ack data")
            client.sendEvent("ackevent2", object : AckCallback<String>(String::class.java) {
                override fun onSuccess(result: String) {
                    println("ack from client: " + client.sessionId + " data: " + result)
                }
            }, ackChatObjectData)

            val ackChatObjectData1 = ChatObject(data.userName, "message with void ack")
            client.sendEvent("hello", object : VoidAckCallback() {
                override fun onSuccess() {
                    println("void ack from: " + client.sessionId)
                }

            }, ackChatObjectData1)
        })

        socketServer!!.start()
        println("socketServer已启动");
        */
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CameraMonitorApplication::class.java, *args)
}




