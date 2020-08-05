package com.dan.spring.boot.nexus

import com.dan.spring.boot.nexus.pojo.User
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(User::class)
open class UserServiceApplication {

}


fun main(args: Array<String>) {
    runApplication<UserServiceApplication>(*args)
}
