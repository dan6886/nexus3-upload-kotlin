package filehandler

import java.io.File

data class UploadItem(val group: String, val art: String = "", val version: String = "", val file: File? = null, val extension: String = "")