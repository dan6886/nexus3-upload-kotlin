package com.dan.spring.boot.nexus.handler

import com.dan.spring.boot.nexus.pojo.NexusPojo
import com.dan.spring.boot.nexus.pojo.UploadItem
import java.io.File

class FileHandler private constructor() {
    /**
     * 按照group区分,如果有字段则只会处理包含对应字段的，如果为空，则不处理任何;如果为 *,则处理任何
     */
    var targetGroup = arrayListOf<String>()
    /**
     * 按照artifact区分,如果有字段则只会处理包含对应字段的，如果为空，则不处理任何;如果为 *,则处理任何
     */
    var targetArtifact = arrayListOf<String>()
    /**
     * 起始根目录
     */
    var dir: File = File("")

    var nexusPojoList: List<NexusPojo> = emptyList()

    private val itemList = arrayListOf<UploadItem>()

    companion object {
        val root = "C:\\Users\\Administrator\\.gradle\\caches\\modules-2\\files-2.1"
    }

    var running: Boolean = false

    fun getAllFile(): List<UploadItem> {
        if (running) {
            return emptyList()
        }
        running = true

        this.startWalk(File(root))
        val copy = arrayListOf<UploadItem>()
        copy.addAll(itemList)
        reset()
        return copy
    }

    fun reset() {
        itemList.clear()
        running = false
    }

    fun startWalk(file: File = dir) {
        val walkTopDown = file
                .walk(FileWalkDirection.TOP_DOWN)
                .maxDepth(1)
                .onEnter {
                    true
                }.filter {
                    if (targetGroup.contains("*")) {
                        return@filter true
                    }
                    targetGroup.forEach { file ->
                        if (it.name.contains(file)) {
                            return@filter true
                        }
                    }
                    return@filter false
                }.forEach {
                    itemList.add(UploadItem(it.name))
                }
//        file.walkTopDown()
        walkByGroup()
    }

    private fun walkByGroup() {
        val tempList = arrayListOf<UploadItem>()
        itemList.forEach {
            val file1 = File(groupPath(it))
            file1.walk(direction = FileWalkDirection.TOP_DOWN)
                    .maxDepth(1)
                    .onEnter {
                        true
                    }
                    .filter { file ->
                        !file.name.equals(it.group)
                    }
                    .filter {
                        if (targetArtifact.contains("*")) {
                            return@filter true
                        }
                        targetArtifact.forEach { word ->
                            if (it.name.contains(word)) {
                                return@filter true
                            }
                        }
                        return@filter false

                    }.forEach { file ->
//                        println("artifact:" + file)
                        tempList.add(it.copy(art = file.name))
                    }
        }

        itemList.clear()
        itemList.addAll(tempList)
        walkByVersion()
    }

    private fun walkByVersion() {
        var tempList = arrayListOf<UploadItem>()
        itemList.forEach {
            File(versionPath(it))
                    .walk(direction = FileWalkDirection.TOP_DOWN)
                    .maxDepth(1)
                    .filter { file ->
                        file.name != it.art
                    }
                    .forEach { file ->
//                        println("version:" + file)
                        tempList.add(it.copy(version = file.name))
                    }
        }
        itemList.clear()
        itemList.addAll(tempList)
        walkByFile()
    }

    private fun walkByFile() {
        var tempList = arrayListOf<UploadItem>()
        itemList.forEach {
            File(filePath(it))
                    .walk(direction = FileWalkDirection.TOP_DOWN)
                    .maxDepth(2)
                    .filter { file ->
                        file.name != it.version
                    }
                    .filter { file ->
                        file.isFile
                    }
                    .filter { file ->
                        "jar" == file.extension || "aar" == file.extension
                    }
                    .filter { file ->
                        !file.name.contains("-sources")
                    }
                    .filter { file ->
                        !file.name.contains("-javadoc")
                    }
                    .forEach { file ->
//                        println("file:" + file)
                        val nexus = NexusPojo(it.group, it.art, it.version)
                        val copy = if (nexusPojoList.contains(nexus)) {
                            println("找到已经上传的内容$it")
                            it.copy(file = file, extension = file.extension, installed = true)
                        } else {
                            it.copy(file = file, extension = file.extension, installed = false)
                        }
                        tempList.add(copy)
                    }
        }
        itemList.clear()
        itemList.addAll(tempList)
    }

    private fun groupPath(uploadItem: UploadItem): String {
        return dir.canonicalPath + File.separator + uploadItem.group
    }

    private fun versionPath(uploadItem: UploadItem): String {
        return dir.canonicalPath + File.separator + uploadItem.group + File.separator + uploadItem.art
    }

    private fun filePath(uploadItem: UploadItem): String {
        return dir.canonicalPath + File.separator + uploadItem.group + File.separator + uploadItem.art + File.separator + uploadItem.version
    }

    fun getItemList(): List<UploadItem> {
        return itemList
    }

    class Builder {
        private var groups = arrayListOf<String>()
        private var artifacts = arrayListOf<String>()
        private var dir: String = ""
        fun addGroup(group: String): Builder {
            groups.add(group)
            return this
        }

        fun addArtifact(art: String): Builder {
            artifacts.add(art)
            return this
        }

        fun rootDir(dir: String): Builder {
            this.dir = dir
            return this
        }

        fun build(): FileHandler {
            val fileHandler = FileHandler()
            fileHandler.targetGroup = groups
            fileHandler.targetArtifact = artifacts
            fileHandler.dir = File(dir)
            return fileHandler
        }
    }
}