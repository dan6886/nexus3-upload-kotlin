package com.spring.example.userservice.beans

import com.spring.example.userservice.beans.CompactDisc
import org.springframework.stereotype.Component

@Component("lonely")
class SgtPepper : CompactDisc {
    var title: String = "love heart"
    var artist: String = "nobody"

    override fun play() {
        println("$this play $title by $artist")
    }
}