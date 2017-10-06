package com.vts.cameramonitor.controller

import com.jsoniter.JsonIterator
import com.vts.cameramonitor.entity.UserEntity
import com.vts.cameramonitor.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/userManage")
class UserManageController {
    class DataJsonString {
        var jsonString:String = "";
    }

    @Autowired
    lateinit var userMapper: UserMapper

    @GetMapping("/userList")
    fun getUsers():List<Any>{
        return userMapper.getAll();
    }

    @PutMapping("/userList")
    fun createUser(@RequestBody data: DataJsonString): Any {
        val tmpData = JsonIterator.deserialize(data.jsonString);
        println("新建用户 " + tmpData.get("userName"));
        val newUser = UserEntity(tmpData.get("userName").toString(),tmpData.get("passWord").toString(),tmpData.get("videoTransform").toInt(),tmpData.get("imageSearch").toInt(),tmpData.get("imageDetect").toInt(),tmpData.get("userManage").toInt(), tmpData.get("createUser").toString());
        return try {
            userMapper.insert(newUser);
            val newId = userMapper.getId(tmpData.get("userName").toString());
            val map = mapOf<String, Any>("result" to true, "id" to newId);
            map;
        } catch (e:Exception) {
            println(e.message);
            val map = mapOf<String, Any>("result" to false, "errorMessage" to e.message!!);
            map;
        }
    }

    @PostMapping("/userList")
    fun updateUser(@RequestBody data:DataJsonString) : Any{
        val tmpData = JsonIterator.deserialize(data.jsonString);
        println("修改用户" + tmpData.get("userName"));
        val newUser = UserEntity(tmpData.get("id").toInt(),tmpData.get("userName").toString(),tmpData.get("passWord").toString(),tmpData.get("videoTransform").toInt(),tmpData.get("imageSearch").toInt(),tmpData.get("imageDetect").toInt(),tmpData.get("userManage").toInt(), tmpData.get("modifyUser").toString());
        return try {
            userMapper.update(newUser);
            val map = mapOf<String, Any>("result" to true);
            map;
        } catch (e:Exception) {
            val map = mapOf<String, Any>("result" to false);
            map;
        }
    }

    @DeleteMapping("/userList")
    fun deleteUser(@RequestBody data:DataJsonString) {

    }
}