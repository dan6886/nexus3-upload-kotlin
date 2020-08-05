package com.dan.spring.boot.nexus.controller

import com.dan.spring.boot.nexus.pojo.UploadItem
import com.dan.spring.boot.nexus.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody


@Controller
@RequestMapping("/file")
class FileController {
    @Autowired
    lateinit var mFileService: FileService

    @RequestMapping("/home")
    fun getUser(map: Model): String {
//        val findAll = mFileService.findAll()
//        map.addAttribute("files", findAll)
        return "fileFind"
    }

    @RequestMapping("/files")
    @ResponseBody
    fun getFiles(): List<UploadItem> {
        val findAll = mFileService.findAll()
        return findAll
    }

    @PostMapping(value = ["/upload"], consumes = arrayOf("application/json"))
    @ResponseBody
    fun upload(@RequestBody uploadItem: UploadItem): String {
        println("upload:" + uploadItem)
        mFileService.upload(uploadItem)
        return ""
    }
}