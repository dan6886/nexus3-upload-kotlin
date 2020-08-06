package com.dan.spring.boot.nexus.handler

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.dan.spring.boot.nexus.pojo.NexusPojo
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import okhttp3.Credentials
import okhttp3.HttpUrl
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
        private const val release_host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan"
        private const val snap_host: String = "http://localhost:8082/service/rest/v1/components?repository=cx-dan-snapshot"
        fun query(token: String? = "", host: String): String {
            val newCall = client.newCall(getRequest(token, host))
            val execute = newCall.execute()
            if (execute.body != null) {
                return execute.body!!.string()
            }
            return ""
        }

        fun getRelease(): Observable<List<NexusPojo>> {
            return Observable.create(ObservableOnSubscribe<List<NexusPojo>> {
                val list = getStepByStep(release_host)
                it.onNext(list)
                it.onComplete()
            }).observeOn(Schedulers.io())
        }

        fun getSnapshot(): Observable<List<NexusPojo>> {
            return Observable.create(ObservableOnSubscribe<List<NexusPojo>> {
                val list = getStepByStep(snap_host)
                it.onNext(list)
                it.onComplete()
            }).observeOn(Schedulers.io())
        }

        private fun getStepByStep(host: String): List<NexusPojo> {
            var token: String? = ""
            val list: MutableList<NexusPojo> = ArrayList()
            do {
                var result = query(token, host)
                var jsonObject: JSONObject = JSONObject.parseObject(result)
                var items: JSONArray = jsonObject.getJSONArray("items")
                token = jsonObject.getString("continuationToken")
                val temp: List<NexusPojo> = JSONArray.parseArray(items.toJSONString(), NexusPojo::class.java)
                list.addAll(temp)
            } while (!StringUtils.isEmpty(token));
            return list
        }


        private fun getRequest(token: String?, host: String): Request {
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