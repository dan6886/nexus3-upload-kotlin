package com.dan.spring.boot.nexus

import com.dan.spring.boot.nexus.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals

@ActiveProfiles(value = ["dev"])
@SpringBootTest
class HelloTest {
    @Autowired
    lateinit var fileService: FileService

    @org.junit.jupiter.api.Test
    fun contextLoads() {
        println(fileService.queryNexus().size)
    }

}
