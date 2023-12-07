package com.myfitnesspal.nyt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    var articleList : List<ArticleData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ItemClickListener? = null

    override fun getItemCount() = articleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_row_item, parent, false)

        val viewHolder = ViewHolder(view)

        view.setOnClickListener {
            viewHolder.articleData?.let {
                itemClickListener?.apply {
                    onClicked(it)
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleData = articleList[position]
        holder.titleView.text = articleData.title
        holder.articleData = articleData
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView = view.findViewById(R.id.titleView)
        var articleData : ArticleData? = null
    }

    interface ItemClickListener {
        fun onClicked(articleData: ArticleData)
    }
}