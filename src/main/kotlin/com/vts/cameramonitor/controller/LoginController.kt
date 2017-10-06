package com.vts.cameramonitor.controller

import com.vts.cameramonitor.entity.UserEntity
import com.vts.cameramonitor.mapper.UserMapper
import org.apache.shiro.SecurityUtils
import javax.servlet.http.HttpSession
import org.apache.shiro.authc.UsernamePasswordToken
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class LoginController{

    @Autowired
    lateinit var userMapper: UserMapper

    private val logger = LoggerFactory.getLogger(LoginController::class.qualifiedName);

    @RequestMapping("/Login")
    @ResponseBody
    fun loginUser( request: HttpServletRequest, response:HttpServletResponse, @RequestParam("userName") username: String,@RequestParam("passWord") password: String, @RequestParam("rememberMe") rememberMe:Boolean): Any {
        val usernamePasswordToken = UsernamePasswordToken(username, password, rememberMe);
        val subject = SecurityUtils.getSubject()
        return try {
            subject.login(usernamePasswordToken);
            val user = subject.principal.toString();
            request.session.setAttribute("user", user);
            val tmpUser = userMapper.getUserByUsername(username);
            if (!tmpUser.isEmpty()) {
                mapOf<String, Any>("result" to true, "user" to tmpUser);
            } else {
                mapOf<String, Any>("result" to false, "errorMessage" to "The user isn't existed.")
            }
        } catch (e: Exception) {
            logger.error(e.message)
            println(e.message)
            mapOf<String,Any>("result" to false, "errorMessage" to e.message!!);
        }

    }

    @RequestMapping("/LogOut")
    fun logOut(request: HttpServletRequest, response: HttpServletResponse): String {
        val subject = SecurityUtils.getSubject()
        subject.logout()
        //        session.removeAttribute("user");
        return "login"
    }
}
