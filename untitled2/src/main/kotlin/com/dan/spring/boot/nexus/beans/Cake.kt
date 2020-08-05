package com.spring.example.userservice.beans

import org.springframework.stereotype.Component

@Component
class Cake : Desert {
    override fun flavor() {
        println("Cake test warn and good")
    }
}