package com.myfitnesspal.nyt

import android.util.JsonReader
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ArticleViewModel : ViewModel() {
    companion object {
        private val TAG = ArticleViewModel::class.java.simpleName
    }

    val articleList : MutableLiveData<List<ArticleData>> by lazy {
        MutableLiveData<List<ArticleData>>()
    }

    /**
     * Fetch articles from [NYT Article Search API](https://developer.nytimes.com/docs/articlesearch-product/1/routes/articlesearch.json/get)
     */
    fun fetchArticles() {
        val url = URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=oFjnnSAIsADysrOAdAF5ihGlecFPNLU4")
        viewModelScope.launch(Dispatchers.IO) {
            val connection = (url.openConnection() as? HttpsURLConnection)
            try {
                connection?.run {
                    readTimeout = 3000
                    connectTimeout = 3000
                    requestMethod = "GET"
                    doInput = true
                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        Log.e(TAG, "HTTP error code: $responseCode")
                        articleList.postValue(emptyList())
                    }
                    inputStream?.let { stream ->
                        val fetchedArticles = mutableListOf<ArticleData>()
                        val reader = JsonReader(BufferedReader(InputStreamReader(stream)))
                        reader.beginObject()
                        var status: String
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "status" -> {
                                    status = reader.nextString()
                                    Log.d(TAG, "status $status")
                                }
                                "response" -> {
                                    reader.beginObject()
                                    while (reader.hasNext()) {
                                        when (reader.nextName()) {
                                            "docs" -> {
                                                reader.beginArray()
                                                var articleTitle: String? = null
                                                var articleUrl: String?
                                                while (reader.hasNext()) {
                                                    reader.beginObject()
                                                    while (reader.hasNext()) {
                                                        when (reader.nextName()) {
                                                            "headline" -> {
                                                                reader.beginObject()
                                                                while (reader.hasNext()) {
                                                                    when (reader.nextName()) {
                                                                        "main" -> articleTitle = reader.nextString()
                                                                        else -> reader.skipValue()
                                                                    }
                                                                }
                                                                reader.endObject()
                                                            }
                                                            "web_url" -> {
                                                                articleUrl = reader.nextString()
                                                                if (articleTitle != null && articleUrl != null) {
                                                                    fetchedArticles.add(ArticleData(articleTitle, articleUrl))
                                                                } else {
                                                                    Log.e(TAG, "Article missing title or url: $articleTitle, $url")
                                                                }
                                                            }
                                                            else -> reader.skipValue()
                                                        }
                                                    }
                                                    reader.endObject()
                                                }
                                                reader.endArray()
                                            }
                                            else -> reader.skipValue()
                                        }
                                    }
                                    reader.endObject()
                                }
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()

                        articleList.postValue(fetchedArticles)
                    }
                }
            } finally {
                connection?.disconnect()
            }
        }
    }
}