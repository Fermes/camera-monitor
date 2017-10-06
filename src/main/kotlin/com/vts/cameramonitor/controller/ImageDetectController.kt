package com.vts.cameramonitor.controller

import com.jsoniter.JsonIterator
import com.vts.cameramonitor.entity.ImageEntity
import com.vts.cameramonitor.mapper.ImageMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/imageDetect")
class ImageDetectController {
    @Autowired
    lateinit var imageMapper: ImageMapper

    private val logger = LoggerFactory.getLogger(ImageDetectController::class.qualifiedName);

    @RequestMapping("/search")
    fun getDetectRecords(@RequestBody data: ImageSearchController.DataJsonString):Any {
        val tmpData = JsonIterator.deserialize(data.jsonString);
        val needHouse = tmpData.get("house").toBoolean();
        val needTree = tmpData.get("tree").toBoolean();
        var imageList:List<ImageEntity>? = null
        try {
            var imageNum = 0;
            if (needHouse && needTree) {
                imageNum = imageMapper.getImageNumWithAll();
                imageList = imageMapper.getDetectRecordsWithAll(tmpData.get("index").toInt(),tmpData.get("pageSize").toInt())
            } else if (needHouse) {
                imageNum = imageMapper.getImageNumWithHouse();
                imageList = imageMapper.getDetectRecordsWithHouse(tmpData.get("index").toInt(),tmpData.get("pageSize").toInt())
            } else if (needTree) {
                imageNum = imageMapper.getImageNumWithTree();
                imageList = imageMapper.getDetectRecordsWithTree(tmpData.get("index").toInt(),tmpData.get("pageSize").toInt())
            }
            return if (imageList != null && imageList.isNotEmpty()) {
                mapOf<String, Any>("result" to true, "imageNum" to imageNum, "imageList" to imageList)
            } else {
                mapOf<String, Any>("result" to false)
            }
        } catch (e:Exception) {
            logger.error(e.message)
            println(e.message)
            return mapOf<String, Any>("result" to false, "errorMessage" to e.message!!);
        }
    }

    @GetMapping("/image")
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

    @DeleteMapping("/image")
    fun deleteImage(@RequestBody data: ImageSearchController.DataJsonString) :Any{
        return try {
            val idList = JsonIterator.deserialize(data.jsonString).get("idList");
            idList.forEach{it -> imageMapper.delete(it.toInt())}
            mapOf<String, Any>("result" to true);
        } catch (e:Exception) {
            logger.error(e.message);
            println(e.message);
            mapOf<String, Any>("result" to false,"errorMessage" to e.message!!);
        }
    }
}