package com.vts.cameramonitor.mapper

import com.vts.cameramonitor.entity.ImageEntity
import com.vts.cameramonitor.entity.ImageSQLEntity
import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Component
import java.sql.Blob

@Component
interface ImageMapper {
    @Select("SELECT primary_image FROM images WHERE id = #{param1}")
    @Results(
            Result(property = "imageByte", column = "primary_image")
    )
    fun getPrimaryImageById(id:Int):ImageSQLEntity

    @Select("SELECT detect_image FROM images WHERE id = #{param1}")
    @Results(
            Result(property = "imageByte", column = "detect_image")
    )
    fun getDetectImageById(id:Int):ImageSQLEntity

    @Select("SELECT COUNT(*) FROM images WHERE detect_result -> '$.house' = 1")
    fun getImageNumWithHouse():Int

    @Select("SELECT COUNT(*) FROM images WHERE detect_result -> '$.tree' = 1")
    fun getImageNumWithTree():Int

    @Select("SELECT COUNT(*) FROM images WHERE detect_result is not null")
    fun getImageNumWithAll():Int

    @Select("SELECT id,name,detect_result,detect_time FROM images WHERE detect_result -> '$.house' = 1 LIMIT #{param1},#{param2}")
    @Results(
            Result(property = "id",column = "id"),
            Result(property = "name",column = "name"),
            Result(property = "detectResult",column = "detect_result"),
            Result(property = "detectTime", column = "detect_time")
    )
    fun getDetectRecordsWithHouse(index:Int, pageSize:Int):List<ImageEntity>

    @Select("SELECT id,name,detect_result,detect_time FROM images WHERE detect_result -> '$.tree' = 1 LIMIT #{param1},#{param2}")
    @Results(
            Result(property = "id",column = "id"),
            Result(property = "name",column = "name"),
            Result(property = "detectResult",column = "detect_result"),
            Result(property = "detectTime", column = "detect_time")
    )
    fun getDetectRecordsWithTree(index:Int, pageSize:Int):List<ImageEntity>

    @Select("SELECT id,name,detect_result,detect_time FROM images WHERE detect_result is not null LIMIT #{param1},#{param2}")
    @Results(
            Result(property = "id",column = "id"),
            Result(property = "name",column = "name"),
            Result(property = "detectResult",column = "detect_result"),
            Result(property = "detectTime", column = "detect_time")
    )
    fun getDetectRecordsWithAll(index:Int, pageSize:Int):List<ImageEntity>

    @Select("SELECT id, name, create_time FROM images ORDER BY create_time DESC LIMIT #{param1},#{param2}")
    @Results(
            Result(property = "id", column = "id"),
            Result(property = "name", column = "name"),
            Result(property = "createTime", column = "create_time")
    )
    fun getImageList(index:Int, pageSize:Int) :List<ImageEntity>;

    @Select("SELECT id, name, primary_image FROM images WHERE create_time BETWEEN #{param3} AND #{param4} LIMIT #{param1}, #{param2} ")
    @Results(
            Result(property = "id", column = "id"),
            Result(property = "name", column = "name"),
            Result(property = "primaryImage", column = "primary_image")
    )
    fun getImageListByCreateTime(index:Int, pageSize: Int, startTime:String, endTime:String):List<ImageEntity>;

    @Select("SELECT id, name, primary_image FROM images WHERE detect_time BETWEEN #{param3} AND #{param4} LIMIT #{param1}, #{param2} ")
    @Results(
            Result(property = "id", column = "id"),
            Result(property = "name", column = "name"),
            Result(property = "primaryImage", column = "primary_image")
    )
    fun getImageListByDetectTime(index:Int, pageSize: Int, startTime:String, endTime:String):List<ImageEntity>;

    @Select("SELECT COUNT(*) FROM images WHERE create_time BETWEEN #{param1} AND #{param2}")
    fun getImageNumByCreateTime(startTime: String, endTime: String):Int

    @Select("SELECT COUNT(*) FROM images WHERE detect_time BETWEEN #{param1} AND #{param2}")
    fun getImageNumByDetectTime(startTime: String, endTime: String):Int

    @Select("SELECT id FROM images WHERE name=#{param1}")
    fun getId(name:String):Int;

    @Insert("INSERT INTO images(name,primary_image,create_user,create_time,modify_user,modify_time) VALUES (#{name},#{primaryImage},#{createUser},#{createTime},#{modifyUser},#{modifyTime})")
    fun insert(image:ImageEntity);

    @Update("UPDATE images SET detect_image=#{detectImage},detect_result=#{detectResult},modify_user=#{modifyUser},detect_user=#{detectUser},detect_time=#{detectTime} WHERE id=#{id}")
    fun detect(image:ImageEntity);

    @Delete("DELETE FROM images WHERE id = #{param1}")
    fun delete(id:Int);
}