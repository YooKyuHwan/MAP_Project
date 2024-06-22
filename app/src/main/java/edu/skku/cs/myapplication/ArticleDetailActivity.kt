package edu.skku.cs.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class ArticleDetailActivity : AppCompatActivity() {
    var userId: String? = null
    var userName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val tvTitle = findViewById<TextView>(R.id.tvTitleDetail)
        val tvAuthor = findViewById<TextView>(R.id.tvAuthorDetail)
        val ivImage = findViewById<ImageView>(R.id.ivImageDetail)
        val tvContent = findViewById<TextView>(R.id.tvContentDetail)
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val articleJson = intent.getStringExtra("article")
        val article = Gson().fromJson(articleJson, Article::class.java)

        val intent = intent
        if(intent.hasExtra("USER_ID") && intent.hasExtra("USER_NAME")){
            userId = intent.getStringExtra("USER_ID")
            userName = intent.getStringExtra("USER_NAME")
            Log.i("loginTeset", userId.toString())
            Log.i("loginTeset", userName.toString())
        }else{
            Log.i("loginTeset", userId.toString())
            Log.i("loginTeset", userName.toString())
        }

        btnSave.setOnClickListener {
            if (userId!=null && userName!=null){
                //Save news article
                ServerClient.addNews(userId!!, article.title.toString(), article.url.toString(), object : Callback{
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            val myToast =
                                Toast.makeText(this@ArticleDetailActivity, "Save This Article", Toast.LENGTH_SHORT)
                            myToast.show()
                        }
                    }

                })
                Log.i("testAddNews", "Good")
            }else{
                val myToast = Toast.makeText(this.applicationContext, "You Have to Login First", Toast.LENGTH_SHORT)
                myToast.show()
                runOnUiThread {
                    val intent = Intent(this@ArticleDetailActivity, LoginActivity::class.java).apply {
                    }
                    startActivity(intent)
                }
                return@setOnClickListener
            }
        }

        tvTitle.text = article.title
        tvAuthor.text = article.author ?: "Unknown Author"
        tvContent.text = article.content

        var str = ""

        val formattedDate = formatDateTime(article.publishedAt.toString())
        tvDate.text = formattedDate
        Glide.with(this).load(article.urlToImage).into(ivImage)
        //og.i("asd", article.publishedAt.toString())

        //textView.text = textContent
        //textView.visibility = View.VISIBLE

        val client = OkHttpClient()
        val req = Request.Builder()
            .url(article.url)
            .build()
        client.newCall(req).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val html = responseBody.string()
                    val doc = Jsoup.parse(html)
                    val pTags = doc.select("p")
                    str = ""
                    for(pTag in pTags){
                        val text = pTag.text()
                        str += text + "\n"
                    }
                    Log.i("myLog", str)
                    CoroutineScope(Dispatchers.Main).launch {
                        tvContent.text = str
                    }
                }
            }
        })

        val btnSummary = findViewById<Button>(R.id.btnSummary)
        btnSummary.setOnClickListener {
            Log.i("wer","wer")
            val title = article.title.toString()
            var content = str
                //article.content.toString()
            Log.i("title", "title: $title")
            Log.i("cont", "cont: $content")

            content = content.substring(0, 1900)
            ClovaSummaryApiClient.summaryText(title, content, object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("ArticleDetailActivity", "API 호출 실패", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    Log.i("sdf", responseData.toString())
                    if (response.isSuccessful && responseData != null) {
                        try {
                            val jsonResponse = JSONObject(responseData)
                            val summary = jsonResponse.getString("summary")
                            Log.i("myLog", "Clova : $summary")
                            CoroutineScope(Dispatchers.Main).launch {
                                tvSummary.text = "요약 내용: \n" + summary
                                tvSummary.visibility = View.VISIBLE
                            }
                        } catch (e: Exception) {
                            Log.e("ArticleDetailActivity", "응답 파싱 실패", e)
                        }
                    } else {
                        //
                    }
                }
            })
        }
    }

    fun formatDateTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        val date: Date? = inputFormat.parse(input)
        return if (date != null) {
            outputFormat.format(date)
        } else {
            "Invalid Date"
        }
    }
}