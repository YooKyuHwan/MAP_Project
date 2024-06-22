package edu.skku.cs.myapplication

import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

object ServerClient {
    private val host = "http://10.0.2.2:8080"
    private val client = OkHttpClient()

    fun getUser(ID: String, PW: String, callback: Callback){
        val request = Request.Builder()
            .url(host + "/users" + "?userId=" + ID + "&userPw=" + PW)
            .build()

        client.newCall(request).enqueue(callback)
    }
}