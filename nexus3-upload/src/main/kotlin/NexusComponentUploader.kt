import NexusComponentUploader.Constant.host
import filehandler.UploadItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException

/**
 * 完美
 */
class NexusComponentUploader {
    private val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        println(it)
                    }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    object Constant {
        const val host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan"
    }

    fun upload(uploadItem: UploadItem) {
        val newCall = client.newCall(getRequest(uploadItem))
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("failed" + e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("ok:" + response.message)
            }
        })
    }


    private fun getRequest(uploadItem: UploadItem): Request {
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("maven2.asset1", uploadItem.file?.name,
                        uploadItem.file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                .addFormDataPart("maven2.generate-pom", "true")
                .addFormDataPart("maven2.groupId", uploadItem.group)
                .addFormDataPart("maven2.artifactId", uploadItem.art)
                .addFormDataPart("maven2.packaging", uploadItem.extension)
                .addFormDataPart("version", uploadItem.version)
                .addFormDataPart("maven2.asset1.extension", uploadItem.extension)
                .build()
        return Request.Builder()
                .url(host)
                .post(body = requestBody)
                .header("Authorization", Credentials.basic("admin", "admin123"))
                .build()

    }
}