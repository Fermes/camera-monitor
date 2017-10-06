package com.vts.cameramonitor.config

import org.apache.shiro.mgt.SecurityManager
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import java.util.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
import org.apache.shiro.spring.LifecycleBeanPostProcessor
import org.apache.shiro.authc.credential.CredentialsMatcher




@Configuration
class ShiroConfig {
    @Bean
    fun shiroFilter(securityManager: org.apache.shiro.mgt.SecurityManager): ShiroFilterFactoryBean {
        println("ShiroConfiguration.shirFilter()")
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager
        //拦截器.
        val filterChainDefinitionMap = LinkedHashMap<String, String>()
        // 配置不会被拦截的链接 顺序判断

        filterChainDefinitionMap.put("/static/**", "anon")
        filterChainDefinitionMap.put("/", "anon")
        filterChainDefinitionMap.put("/Login", "anon")
        filterChainDefinitionMap.put("/Index", "anon")
        filterChainDefinitionMap.put("/videoTransform/imageList", "perms[video_transform]");
        filterChainDefinitionMap.put("/videoTransform/uploadVideo", "perms[video_transform]");
        filterChainDefinitionMap.put("/videoTransform/importVideo", "perms[video_transform]");
        filterChainDefinitionMap.put("/videoTransform/uploadVideo", "perms[video_transform]");
        filterChainDefinitionMap.put("/videoTransform/getVideo", "anon")
        filterChainDefinitionMap.put("/imageSearch/search", "perms[image_search]");
        filterChainDefinitionMap.put("/imageSearch/getImage", "anon")
        filterChainDefinitionMap.put("/imageDetect/**", "perms[image_detect]");
        filterChainDefinitionMap.put("/userManage/**", "perms[user_manage]");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout")
        //过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/**", "authc")
        //filterChainDefinitionMap.put("/**", "anon")

        shiroFilterFactoryBean.loginUrl = "/Login"
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.successUrl = "/"

        //未授权界面;
        //shiroFilterFactoryBean.unauthorizedUrl = "/403"
        shiroFilterFactoryBean.filterChainDefinitionMap = filterChainDefinitionMap
        return shiroFilterFactoryBean
    }

    @Bean
    fun myShiroRealm(): MyShiroRealm {
        return MyShiroRealm()
    }

    @Bean
    fun securityManager(): SecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setRealm(myShiroRealm())
        return securityManager
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    fun authorizationAttributeSourceAdvisor(securityManager: SecurityManager): AuthorizationAttributeSourceAdvisor {
        val authorizationAttributeSourceAdvisor = AuthorizationAttributeSourceAdvisor()
        authorizationAttributeSourceAdvisor.securityManager = securityManager
        return authorizationAttributeSourceAdvisor
    }

    @Bean(name = arrayOf("simpleMappingExceptionResolver"))
    fun createSimpleMappingExceptionResolver(): SimpleMappingExceptionResolver {
        val r = SimpleMappingExceptionResolver()
        val mappings = Properties()
        mappings.setProperty("DatabaseException", "databaseError")//数据库异常处理
        mappings.setProperty("UnauthorizedException", "403")
        r.setExceptionMappings(mappings)  // None by default
        r.setDefaultErrorView("error")    // No default
        r.setExceptionAttribute("ex")     // Default is "exception"
        //r.setWarnLogCategory("example.MvcLogger");     // No default
        return r
    }
}
