package com.vts.cameramonitor.controller

import com.jsoniter.JsonIterator
import com.vts.cameramonitor.mapper.ImageMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/imageSearch")
class ImageSearchController {

    @Autowired
    lateinit var imageMapper: ImageMapper

    private val logger = LoggerFactory.getLogger(ImageSearchController::class.qualifiedName);
    class DataJsonString {
        var jsonString:String = "";
    }

    @PostMapping("/search")
    fun imageSearch(@RequestBody data:DataJsonString) :Any {
        val tmpData = JsonIterator.deserialize(data.jsonString);
        return try {
            if (tmpData.get("type").toInt() == 0) {
                val imageList = imageMapper.getImageListByCreateTime(tmpData.get("index").toInt(),tmpData.get("pageSize").toInt(), tmpData.get("startTime").toString(), tmpData.get("endTime").toString());
                val imageNum = imageMapper.getImageNumByCreateTime(tmpData.get("startTime").toString(), tmpData.get("endTime").toString())
                mapOf<String, Any>("result" to true, "imageNum" to imageNum, "imageList" to imageList);
            } else {
                val imageList = imageMapper.getImageListByDetectTime(tmpData.get("index").toInt(),tmpData.get("pageSize").toInt(), tmpData.get("startTime").toString(), tmpData.get("endTime").toString());
                val imageNum = imageMapper.getImageNumByDetectTime(tmpData.get("startTime").toString(), tmpData.get("endTime").toString())
                mapOf<String, Any>("result" to true, "imageNum" to imageNum, "imageList" to imageList);
            }
        } catch (e:Exception) {
            logger.error(e.message);
            println(e.message);
            mapOf<String, Any>("result" to false, "errorMessage" to e.message!!);
        }
    }

    @RequestMapping("/getImage")
    fun getImage(request: HttpServletRequest, response: HttpServletResponse, @RequestParam("type") type:Int = 0, @RequestParam("id") id:Int){
        response.contentType = "image/jpg";
        val os : ServletOutputStream = response.outputStream;
        return try {
            if(type == 0) {
                os.write(imageMapper.getPrimaryImageById(id).imageByte);
            } else if (type == 1){
                os.write(imageMapper.getDetectImageById(id).imageByte);
            }
            os.close();
        } catch (e:Exception){
            logger.error(e.message);
            println(e.message);
        } finally {

        }
    }
}