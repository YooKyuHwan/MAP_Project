package edu.skku.cs.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MyNewsPageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_news_page)

        recyclerView = findViewById(R.id.rvMyPage)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyPageAdapter(emptyList())
        recyclerView.adapter = adapter

        val userId = intent.getStringExtra("USER_ID")
        val userName = intent.getStringExtra("USER_NAME")

        val btnBack = findViewById<Button>(R.id.btnGoMain)
        btnBack.setOnClickListener {
            runOnUiThread {
                val intent = Intent(this@MyNewsPageActivity, MainActivity::class.java).apply {
                    putExtra("USER_ID", userId)
                    putExtra("USER_NAME", userName)
                }
                startActivity(intent)
            }
        }

        //test
        ServerClient.getUserNews(userId.toString(), object : Callback{
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val res = responseBody.string()
                    val jsonArray = JSONArray(res)
                    val articleList = mutableListOf<ArticleData>()

                    for (i in 0..jsonArray.length()-1){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        val title = jsonObject.getString("title")
                        val url = jsonObject.getString("url")
                        val data = ArticleData(title, url)
                        articleList.add(data)

                        Log.i("testGetUserNews", title.toString())
                        Log.i("testGetUserNews", url.toString())
                    }

                    runOnUiThread {
                        adapter.updateData(articleList) // 데이터 변경 알림
                    }
                }
            }

        })
        //
    }

    data class ArticleData(
        val title: String,
        val url: String
    )
}