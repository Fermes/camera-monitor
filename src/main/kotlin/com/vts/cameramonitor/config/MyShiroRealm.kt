package com.vts.cameramonitor.config

import com.vts.cameramonitor.entity.UserEntity
import com.vts.cameramonitor.mapper.UserMapper
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import javax.annotation.Resource

class MyShiroRealm:AuthorizingRealm() {
    @Resource
    lateinit var userMapper:UserMapper;

    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
        val authorizationInfo = SimpleAuthorizationInfo();
        val userName = principals.primaryPrincipal.toString();
        val user = userMapper.getUserByUsername(userName)[0];
        authorizationInfo.addRole("user");
        if (user.videoTransform) {
            authorizationInfo.addStringPermission("video_transform");
        }
        if(user.imageSearch) {
            authorizationInfo.addStringPermission("image_search");
        }
        if(user.imageDetect) {
            authorizationInfo.addStringPermission("image_detect");
        }
        if(user.userManage) {
            authorizationInfo.addStringPermission("user_manage");
        }
        return authorizationInfo;
    }

    override protected fun doGetAuthenticationInfo(token:AuthenticationToken) : AuthenticationInfo?{ println("MyShiroRealm.doGetAuthenticationInfo()");
        val username = token.principal.toString();
        println(token.credentials);
        val userList = userMapper.getUserByUsername(username);
        if(userList.isEmpty()) {
            return null;
        }
        val authenticationInfo = SimpleAuthenticationInfo(
               userList[0].userName,userList[0].passWord,super.getName()
        );
        return authenticationInfo;
    }
}