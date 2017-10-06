package com.vts.cameramonitor.controller

import com.alibaba.fastjson.JSON
import com.jsoniter.JsonIterator
import com.vts.cameramonitor.entity.ImageEntity
import com.vts.cameramonitor.mapper.ImageMapper
import com.vts.cameramonitor.service.impl.VideoProcessServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/videoTransform")
class VideoTransformController {
    var video = VideoProcessServiceImpl();

    private val logger = LoggerFactory.getLogger(VideoTransformController::class.qualifiedName);

    @Autowired
    lateinit var imageMapper:ImageMapper

    class DataJsonString {
        var jsonString:String = "";
    }

    @GetMapping("/imageList")
    fun getImageList(@RequestParam("index") index:Int, @RequestParam("pageSize") pageSize:Int):Any{
        return try {
            val res =imageMapper.getImageList(index, pageSize);
            if (video.isOpened) {
                mapOf<String, Any>("result" to true, "isVideoOpen" to true, "imageList" to res);
            } else {
                mapOf<String, Any>("result" to true, "isVideoOpen" to false, "imageList" to res);
            }
        } catch (e:Exception) {
            println(e.message);
            mapOf<String, Any>("result" to false, "errorMessage" to e.message!!);
        }
    }

    data class wsImage(var id:Int, var name: String, var createTime: String);
    @GetMapping("/saveImage")
    @ResponseBody
    fun saveImage(request: HttpServletRequest, response: HttpServletResponse, @RequestParam(value = "createUser", required = false) createUser: String = ""):Any{
        if(!video.isOpened) {
            return mapOf<String, Any>("result" to false, "errorMessage" to "视频不可用");
        }
        //while (video.isReading);
       // video.isReading = true;
        return try {
            if (createUser == "") {
                return mapOf("result" to false, "errorMessage" to "请提供用户名");
            }
            val time:String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            val name:String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".jpg";
            val image = ImageEntity(name, video.imageByteArray, createUser);
            imageMapper.insert(image)
            val id = imageMapper.getId(name);

            val results = mutableMapOf<String,Int>();
            val houseRatio = Math.random() * 10;
            val treeRatio = Math.random() * 10;
            if(houseRatio < 6) {
                results.put("house", 1);
            } else {
                results.put("house", 0);
            }
            if(treeRatio < 5) {
                results.put("tree", 1);
            }else{
                results.put("tree", 0);
            }

            val resStr = JSON.toJSONString(results);
            val detectImage = ImageEntity(id,video.imageByteArray,resStr,createUser);
            imageMapper.detect(detectImage);
            //socketServer!!.broadcastOperations.sendEvent("new-image", wsImage(id ,name,time));
            mapOf<String, Any>("result" to true, "data" to wsImage(id ,name,time));
        } catch (e:Exception){
            logger.error(e.message);
            println(e.message);
            mapOf<String, Any>("result" to false, "errorMessage" to e.message!!);
        } finally {
            //video.isReading = false;
        }
    }

    @RequestMapping("/uploadVideo")
    fun uploadVideo(@RequestParam("createUser")createUser: String, @RequestParam(value = "video", required = false) uploadVideo:MultipartFile, request: HttpServletRequest, response: HttpServletResponse):Any {
        return try {
            //val filePath = request.session.servletContext.getRealPath("/upload");
            val filePath = "D:/camera_upload_temp";
            val targetFile = File(filePath);
            if (!targetFile.exists()) {
                targetFile.mkdirs()
            }
           /* val out = FileOutputStream(filePath + "01.mp4");
            out.write(primaryImage.bytes);
            out.flush()
            out.close()*/
            if (video.isOpened) {
                video.isStop = true;
                video = VideoProcessServiceImpl();
            }
            val fileSource = File(filePath, "01.mp4");
            uploadVideo.transferTo(fileSource);
            if (video.openVideo(fileSource.path)) {
                video.start();
                mapOf<String, Any>("result" to true)
            } else {
                throw Exception("视频打开错误");
            }
        } catch (e:Exception) {
            println(e.message);
            mapOf<String,Any>("result" to false, "errorMessage" to e.message!!);
        }
    }

    @GetMapping("/importVideo")
    fun importVideo(@RequestParam("address") videoAddress:String): Any {
        if (video.isOpened) {
            video.isStop = true;
            video = VideoProcessServiceImpl();
        }
        return if (video.openVideo(videoAddress)) {
            video.start();
            mapOf<String, Any>("result" to true);
        } else {
            mapOf<String, Any>("result" to false, "errorMessage" to "打开视频失败");
        }
    }

    @RequestMapping("/getVideo")
    fun getVideo(request: HttpServletRequest, response: HttpServletResponse):Any{
        if (!video.isOpened) {
            //response.sendRedirect("/video.jpg");
            return ModelAndView("/video.jpg");
        }
        response.contentType = "image/jpg";
        val os : ServletOutputStream = response.outputStream;
        return try {
            os.write(video.imageByteArray);
            os.close();
        } catch (e:Exception){

        } finally {

        }
    }
}