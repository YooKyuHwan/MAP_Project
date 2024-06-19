package edu.skku.cs.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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
import org.jsoup.Jsoup
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ArticleDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val tvTitle = findViewById<TextView>(R.id.tvTitleDetail)
        val tvAuthor = findViewById<TextView>(R.id.tvAuthorDetail)
        val ivImage = findViewById<ImageView>(R.id.ivImageDetail)
        val tvContent = findViewById<TextView>(R.id.tvContentDetail)
        val tvSummary = findViewById<TextView>(R.id.tvSummary)
        val tvDate = findViewById<TextView>(R.id.tvDate)

        val articleJson = intent.getStringExtra("article")
        val article = Gson().fromJson(articleJson, Article::class.java)

        tvTitle.text = article.title
        tvAuthor.text = article.author ?: "Unknown Author"
        tvContent.text = article.content

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
                    var str = ""
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