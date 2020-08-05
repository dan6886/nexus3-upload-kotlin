package com.spring.example.userservice.beans

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CDPlayer(@Autowired val player: CompactDisc) : MediaPlayer {
    override fun play() {
        player.play()
    }
}