package edu.skku.cs.myapplication

import com.google.gson.Gson
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object ServerClient {
    private val host = "http://10.0.2.2:8080"
    private val client = OkHttpClient()

    fun getUser(ID: String, PW: String, callback: Callback){
        val request = Request.Builder()
            .url(host + "/users" + "?userId=" + ID + "&userPw=" + PW)
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun getUserNews(id: String, callback: Callback){
        val request = Request.Builder()
            .url(host + "/users/newslist" + "?userId=" + id)
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun addNews(id: String, title: String, url: String, callback: Callback){
        val data = AddedArticleDTo(id, title, url)

        val request = Request.Builder()
            .url(host + "/users/addNews")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), Gson().toJson(data)))
            .build()

        client.newCall(request).enqueue(callback)
    }
    data class AddedArticleDTo(
        val id: String,
        val title: String,
        val url: String
    )
}