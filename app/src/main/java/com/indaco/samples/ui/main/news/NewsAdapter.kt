package com.indaco.samples.ui.main.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.indaco.samples.data.models.news.News
import com.indaco.samples.databinding.RowItemNewsBinding

class NewsAdapter(var newsList: MutableList<News>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    class NewsViewHolder(val binding: RowItemNewsBinding): RecyclerView.ViewHolder(binding.root)

    fun updateNews(list: List<News>) {
        newsList.addAll(0, list)
        notifyItemRangeInserted(0, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(RowItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(holder.binding) {
            newsTv.text = newsList[position].title
        }
    }

    override fun getItemCount() = newsList.size

    fun clearNews() {
        newsList.clear()
        notifyDataSetChanged()
    }
}