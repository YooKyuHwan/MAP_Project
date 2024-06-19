package edu.skku.cs.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}