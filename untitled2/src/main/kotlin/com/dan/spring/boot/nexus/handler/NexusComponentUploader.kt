package com.dan.spring.boot.nexus.handler

import com.dan.spring.boot.nexus.pojo.UploadItem
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException

/**
 * 上传
 */
@Component
class NexusComponentUploader {
    @Value("\${nexus.release.host}")
    fun setReleaseHost(host: String?) {
        if (host != null) {
            NexusComponentUploader.release_host = host
        }
    }

    @Value("\${nexus.snapshot.host}")
    fun setSnapShotHost(host: String?) {
        if (host != null) {
            NexusComponentUploader.snap_host = host
        }
    }

    companion object Constant {
        private val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            println(it)
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        private var release_host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan"
        private var snap_host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan-snapshot"
        fun upload(uploadItem: UploadItem, callback: Callback) {
            val newCall = client.newCall(getRequest(uploadItem))
            newCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("failed" + e)
                    callback.onFailure(call, e)
                }

                override fun onResponse(call: Call, response: Response) {
                    println("ok:" + response.message)
                    callback.onResponse(call, response)
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

            var realHost = ""
            if (uploadItem.version.toLowerCase().contains("snapshot")) {
                realHost = snap_host
            } else {
                realHost = release_host
            }
            return Request.Builder()
                    .url(realHost)
                    .post(body = requestBody)
                    .header("Authorization", Credentials.basic("admin", "admin123"))
                    .build()

        }
    }


}