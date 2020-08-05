import filehandler.FileHandler

/**
 * 扫描本地缓存Jar,按照规则上传到NEXUS服务器
 */
fun main() {
    // 根目录
    val root = "C:\\Users\\Administrator\\.gradle\\caches\\modules-2\\files-2.1"
    println("work start")
    val file = FileHandler.Builder()
            .rootDir(root)
            .addGroup("*")
            .addArtifact("*")
            .build()
    file.startWalk()
    val itemList = file.getItemList()
    val uploader = NexusComponentUploader()
    itemList.forEach {
//        uploader.upload(it)
    }
}