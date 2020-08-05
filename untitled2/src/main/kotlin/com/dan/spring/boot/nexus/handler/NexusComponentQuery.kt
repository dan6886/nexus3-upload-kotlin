package com.dan.spring.boot.nexus.handler

import okhttp3.Credentials
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.util.StringUtils


/**
 * 上传
 */
class NexusComponentQuery {

    companion object Constant {
        private val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            println(it)
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        const val host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan"
        fun query(token: String = ""): String {
            val newCall = client.newCall(getRequest(token))
            val execute = newCall.execute()
            if (execute.body != null) {
                return execute.body!!.string()
            }
            return ""
        }


        private fun getRequest(token: String): Request {
            val urlBuilder = host.toHttpUrlOrNull()?.newBuilder()
            if (!StringUtils.isEmpty(token)) {
                urlBuilder!!.addQueryParameter("continuationToken", token)
            }
            return Request.Builder()
                    .url(urlBuilder!!.build())
                    .header("Authorization", Credentials.basic("admin", "admin123"))
                    .build()
        }
    }


}