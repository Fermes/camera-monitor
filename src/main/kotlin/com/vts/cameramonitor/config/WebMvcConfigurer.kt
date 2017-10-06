package com.vts.cameramonitor.config

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.socket.server.standard.ServerEndpointExporter
import javax.servlet.MultipartConfigElement

@Configuration
class WebMvcConfigurer : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>?) {
        val converter = FastJsonHttpMessageConverter()
        //自定义配置...
        //FastJsonConfig config = new FastJsonConfig();
        //config.set ...
        //converter.setFastJsonConfig(config);
        converters!!.add(converter)
    }
    /*
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }
    */
    * */
    @Bean
    fun serverEndpointExporter(): ServerEndpointExporter {
        return ServerEndpointExporter()
    }

    @Bean
    fun multipartConfigElement():MultipartConfigElement{
        val config = MultipartConfigFactory();
        config.setMaxFileSize("4096Mb");
        config.setMaxRequestSize("4096Mb");
        return config.createMultipartConfig();
    }
}