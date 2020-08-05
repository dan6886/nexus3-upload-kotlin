package com.spring.example.userservice.utils

import java.util.*

class Collections {
    companion object {
        open fun <K, V> printMap(map: Map<K, V>): Unit {
            println(map)
            map.forEach {
                println(it.key.toString() + ":" + it.value.toString())
            }
        }
    }
}