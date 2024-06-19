package edu.skku.cs.myapplication

import android.util.Log
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

object ClovaSummaryApiClient {
    private val host = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize"
    private val clientId = ""
    private val clientSecret = ""
    private val client = OkHttpClient()

    fun summaryText(title:String, content: String, callback: Callback) {
        val url = host
        //POST body
        val json = JSONObject().apply {
            put("document", JSONObject().apply {
                put("title", title)
                put("content", content)
            })
            put("option", JSONObject().apply {
                put("language", "ko")
                put("model", "news")
            })
        }
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

        //POST Header
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("X-NCP-APIGW-API-KEY-ID", clientId)
            .addHeader("X-NCP-APIGW-API-KEY", clientSecret)
            .addHeader("Content-Type", "application/json")
            .build()

        Log.i("ewrsdf", request.toString())
        Log.i("ewrsdf", json.toString())
        client.newCall(request).enqueue(callback)
    }
}