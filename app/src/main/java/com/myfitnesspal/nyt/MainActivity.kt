package com.myfitnesspal.nyt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleViewModel: ArticleViewModel by viewModels()

        setContentView(R.layout.activity_main)
        val articleList = findViewById<RecyclerView>(R.id.article_list)
        val articleAdapter = ArticleAdapter()
        articleList.adapter = articleAdapter
        articleList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        articleAdapter.itemClickListener = object: ArticleAdapter.ItemClickListener {
            override fun onClicked(articleData: ArticleData) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(articleData.url))
                startActivity(browserIntent)
            }
        }

        articleViewModel.articleList.observe(this) {
            articleAdapter.articleList = it
        }

        articleViewModel.fetchArticles()
    }
}
