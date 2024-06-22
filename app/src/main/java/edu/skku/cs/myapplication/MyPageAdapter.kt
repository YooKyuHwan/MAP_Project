package edu.skku.cs.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyPageAdapter (var articles: List<MyNewsPageActivity.ArticleData>) : RecyclerView.Adapter<MyPageAdapter.ViewHolder>(){
    private lateinit var onItemClickListener: MyPageAdapter.OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    fun updateData(newArticles: List<MyNewsPageActivity.ArticleData>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_mypage, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitleMyPage)
        private val tvUrl: TextView = itemView.findViewById(R.id.tvUrlMyPage)

        init {
            itemView.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener.onItemClick(adapterPosition)
                }
            }
        }

        fun bind(article: MyNewsPageActivity.ArticleData) {
            tvTitle.text = article.title
            tvUrl.text = article.url

            //Event When Click
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                itemView.context.startActivity(intent)
            }
        }

    }
}