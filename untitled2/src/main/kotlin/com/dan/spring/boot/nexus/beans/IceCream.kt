package com.spring.example.userservice.beans

import org.springframework.stereotype.Component

@Component
class IceCream : Desert {
    override fun flavor() {
        println("IceCream test cold and sweet")
    }
}