package com.spring.example.userservice.controller

import com.dan.spring.boot.nexus.pojo.LoginInfo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model


@Controller

class UserController {
//    @RequestMapping("/home")
    fun getUser(map: Model): String {
        val user = LoginInfo("", "")
        map.addAttribute("userInfo", user)
        return "index"
    }

//    @RequestMapping("/login")
    fun login(loginInfo: LoginInfo, model: Model): String {
        println(loginInfo)
        model.addAttribute("url", "home")
        return "loginFailed"
    }
}