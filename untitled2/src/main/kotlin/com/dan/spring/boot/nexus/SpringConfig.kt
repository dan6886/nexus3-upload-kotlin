package com.dan.spring.boot.nexus


import com.dan.spring.boot.nexus.interceptor.FunInterceptor
import com.dan.spring.boot.nexus.pojo.User
import com.dan.spring.boot.nexus.pojo.Worker
import com.spring.example.userservice.beans.Desert
import com.spring.example.userservice.beans.IceCream
import com.dan.spring.boot.nexus.handler.FileHandler
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ComponentScan
@EnableConfigurationProperties(User::class)
open class SpringConfig {
    @Bean
    open fun getDesert(): Desert {
        return IceCream()
    }

    /**
     * 根据属性名称和类的属性进行绑定
     */
    @Bean
    @ConfigurationProperties(prefix = "work.worker")
    open fun getWorker(): Worker {
        return Worker()
    }

    @Bean
    open fun getFileHandler(): FileHandler {
        return FileHandler.Builder()
                .addGroup("*")
                .addArtifact("*")
                .rootDir(FileHandler.root)
                .build()
    }

}


//    /**： 匹配所有路径
//-   /admin/**：匹配 /admin/ 下的所有路径
//-   /secure/*：只匹配 /secure/user，不匹配 /secure/user/info
//*/
@Configuration
open class WebMvcConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(FunInterceptor())
                .addPathPatterns("/**")
    }
}