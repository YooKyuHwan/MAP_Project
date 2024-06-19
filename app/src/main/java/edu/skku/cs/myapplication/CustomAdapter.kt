package edu.skku.cs.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CustomAdapter (private var articles: List<Article>) : RecyclerView.Adapter<CustomAdapter.NewsViewHolder>(){
    private lateinit var onItemClickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }


    fun updateData(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Article {
        return articles[position]
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivNewsImage: ImageView = itemView.findViewById(R.id.ivNewsImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitleDetail)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

        init {
            itemView.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener.onItemClick(adapterPosition)
                }
            }
        }

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvDescription.text = article.description
            Glide.with(itemView.context).load(article.urlToImage).into(ivNewsImage)
        }

    }

}