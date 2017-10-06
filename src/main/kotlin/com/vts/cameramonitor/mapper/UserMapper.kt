package com.vts.cameramonitor.mapper

import com.vts.cameramonitor.entity.UserEntity
import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Component

@Component
interface UserMapper {
    @Select("Select * FROM users")
    @Results(
            Result(property = "id", column = "id"),
            Result(property = "userName", column = "username"),
            Result(property = "passWord", column = "password"),
            Result(property = "videoTransform", column = "video_transform"),
            Result(property = "imageSearch", column = "image_Search"),
            Result(property = "imageDetect", column = "image_detect"),
            Result(property = "userManage", column = "user_manage"),
            Result(property = "createUser", column = "create_user"),
            Result(property = "createTime", column = "create_time"),
            Result(property = "modifyUser", column = "modify_user"),
            Result(property = "modifyTime", column = "modify_time")
    )
    fun getAll(): List<UserEntity>;

    @Select("SELECT id,username,password,video_transform,image_search,image_detect,user_manage FROM users WHERE username = #{param1}")
    @Results(
            Result(property = "id", column = "id"),
            Result(property = "userName", column = "username"),
            Result(property = "passWord", column = "password"),
            Result(property = "videoTransform", column = "video_transform"),
            Result(property = "imageSearch", column = "image_Search"),
            Result(property = "imageDetect", column = "image_detect"),
            Result(property = "userManage", column = "user_manage")
    )
    fun getUserByUsername(username:String):List<UserEntity>;

    @Select("SELECT id FROM users WHERE username = #{param1}")
    fun getId(username:String):Int;

    @Insert("INSERT INTO users(username,password,video_transform,image_search,image_detect,user_manage,create_user,create_time, modify_user, modify_time) VALUES (#{userName}, #{passWord}, #{videoTransform}, #{imageSearch}, #{imageDetect}, #{userManage}, #{createUser}, #{createTime},  #{modifyUser}, #{modifyTime})")
    fun insert(user: UserEntity);

    @Update("UPDATE users SET username=#{userName},password=#{passWord},video_transform=#{videoTransform},image_search=#{imageSearch},image_detect=#{imageDetect},user_manage=#{userManage},modify_user=#{modifyUser} WHERE id = #{id}")
    fun update(user:UserEntity);
}