package edu.skku.cs.myapplication

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException

object NewsApiClient {
    private val host = "https://newsapi.org/v2/"
    private val apiKey = ""
    private val client = OkHttpClient()

    fun searchNews(keyword: String, callback: Callback) {
        val url = (host + "everything").toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("q", keyword)
            .addQueryParameter("apiKey", apiKey)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(callback)
    }
}