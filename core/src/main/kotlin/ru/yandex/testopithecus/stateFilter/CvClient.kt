package ru.yandex.testopithecus.stateFilter

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Request

class CvClient(private val cvServerAddress: String) {

    val JSON = MediaType.get("application/json; charset=utf-8")

    var client = OkHttpClient()

    fun post(json: String): String {
        val body = RequestBody.create(JSON, json)
        val request = Request.Builder()
                .url(cvServerAddress)
                .post(body)
                .build()
        client.newCall(request).execute().use { response -> return response.body()!!.string() }
    }
}