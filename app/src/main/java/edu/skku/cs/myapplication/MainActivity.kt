package edu.skku.cs.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var userKeyWord = findViewById<EditText>(R.id.etKeyword)
        var btnSearch = findViewById<Button>(R.id.btnSearch)
        var newsList = findViewById<RecyclerView>(R.id.rvNews)

        val client = OkHttpClient()
        var path = ""

        //https://newsapi.org/v2/everything?q=Apple&from=2024-06-19&sortBy=popularity&apiKey=API_KEY
        btnSearch.setOnClickListener {
            val keyWord = userKeyWord.text.toString()
            if (keyWord.isEmpty()){
                return@setOnClickListener
            }
            //var reqUrl = NewAPIHost + "everything" + "?q=" + keyWord + "&" + "apiKey=" + apiKey
            NewsApiClient.searchNews(keyWord, object : Callback{
                override fun onFailure(call: Call, e: IOException){
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()
                    Log.d("MainActivity", "Response: $res")
                    if (res != null) {
                        try {
                            val newsResponse = Gson().fromJson(res, NewsResponse::class.java)
                            //Log.i("MyLog", newsResponse.articles[0].author.toString())
                            runOnUiThread {
                                val ad = CustomAdapter(newsResponse.articles)
                                newsList.apply {
                                    layoutManager = LinearLayoutManager(this@MainActivity)
                                    adapter = ad
                                }
                            }

                        } catch (e: JsonSyntaxException) {
                            Log.e("MainActivity", "JSON parsing error", e)
                        }
                    }
                }
            })
        }
    }
}