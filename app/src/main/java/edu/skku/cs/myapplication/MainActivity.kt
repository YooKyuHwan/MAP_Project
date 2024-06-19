package edu.skku.cs.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    private lateinit var customAdapter: CustomAdapter
    private lateinit var newsList: RecyclerView
    private lateinit var userKeyWord: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userKeyWord = findViewById<EditText>(R.id.etKeyword)
        var btnSearch = findViewById<Button>(R.id.btnSearch)
        newsList = findViewById<RecyclerView>(R.id.rvNews)
        newsList.layoutManager = LinearLayoutManager(this)

        customAdapter = CustomAdapter(emptyList())
        newsList.adapter = customAdapter


        //To Handel user click on News
        customAdapter.setOnItemClickListener(object : CustomAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("myLog", "Click Item idx: $position")
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java).apply {
                    putExtra("article", Gson().toJson(customAdapter.getItem(position)))
                }
                startActivity(intent)
            }
        })

        //https://newsapi.org/v2/everything?q=Apple&from=2024-06-19&sortBy=popularity&apiKey=API_KEY
        btnSearch.setOnClickListener {
            val keyWord = userKeyWord.text.toString()
            if (keyWord.isEmpty()){
                Toast.makeText(this, "Please enter a keyword", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            NewsApiClient.searchNews(keyWord, object : Callback{
                override fun onFailure(call: Call, e: IOException){
                }

                override fun onResponse(call: Call, response: Response) {
                    val res = response.body?.string()
                    Log.d("MainActivity", "Response: $res")
                    
                    if(res == null){
                        Toast.makeText(this@MainActivity, "There is no Article on that Keyword", Toast.LENGTH_SHORT).show()
                        return
                    }

                    try {
                        val newsResponse = Gson().fromJson(res, NewsResponse::class.java)
                        //Log.i("MyLog", newsResponse.articles[0].author.toString())

                        if(newsResponse.totalResults == 0){
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "No articles found", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            runOnUiThread {
                                customAdapter.updateData(newsResponse.articles)
                            }
                        }
                    } catch (e: JsonSyntaxException) {
                        Log.e("MainActivity", "JSON parsing error", e)
                    }
                }
            })
        }
    }
}