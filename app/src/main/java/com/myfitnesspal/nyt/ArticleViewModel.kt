package com.myfitnesspal.nyt

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.gson.stream.JsonReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

@HiltViewModel
class ArticleViewModel @Inject constructor(val articleRepository: ArticleRepository): ViewModel() {
    companion object {
        private val TAG = ArticleViewModel::class.java.simpleName
    }

    ///
    //Data is Stored in the ViewModel
    //1 LiveData
    val articleList: MutableLiveData<List<ArticleData>> by lazy {
        MutableLiveData<List<ArticleData>>()
    }
    val articleListFlow = articleList.asFlow()

    //2 StateFlow
    val articles: MutableStateFlow<List<ArticleData>> by lazy {
        MutableStateFlow<List<ArticleData>>(emptyList())
    }


    //Data is Stored in the Repository (and DB)
    //3 StateFlow - Hot Flow from Cold Repo - implicit collection to StateFlow
    val repoArticles: StateFlow<List<ArticleData>> = articleRepository.articles.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    //4 State Flow - Cold Flow from Cold Repo - explicit collection to StateFlow
    val repoArticleList: MutableStateFlow<List<ArticleData>> by lazy {
        MutableStateFlow<List<ArticleData>>(emptyList())
    }
    ///

    //StateFlow of 1 2 3 4.  //should be articles and isLoading
    val articleState = combine(articleListFlow, articles, repoArticles, repoArticleList) { articleListFlow, articles, repoArticles, repoArticleList ->
        listOf(articleListFlow, articles, repoArticles, repoArticleList)
    }

    val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
    )

    //TODO: articleState and isLoading as a State Object Class


    init {
        //fetchArticles()
        viewModelScope.launch {
            //explicit collection of articles to a state
            launch {
                articleRepository.articles.collect {
                    repoArticleList.value = it //4
                    println("Collecting it:-LaunchViewModel4 $it")
                }
            }

            getData()
        }
    }

    fun getData(){
        viewModelScope.launch {
            _isLoading.value = true
            ///
            //Repository Data
            val fetchedArticles = articleRepository.fetchArticles() //3 and 4 - stored in viewModel/Db

            //ViewModel Data
            articleList.postValue(fetchedArticles) //1
            articles.value = fetchedArticles //2
            ///

            _isLoading.value = false
        }
    }





    //-- Refactored --



    /**
     * Fetch articles from [NYT Article Search API](https://developer.nytimes.com/docs/articlesearch-product/1/routes/articlesearch.json/get)
     */
    fun fetchArticles() {

        //Retrofit --
        //Repository --
        //Response Object -- > JSONOBJECT
        //Network Module --
        //BuildConfig Api Key --> BuildConfig.API_KEY
        val url =
            URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=oFjnnSAIsADysrOAdAF5ihGlecFPNLU4")
        viewModelScope.launch(Dispatchers.IO) {
            val connection = (url.openConnection() as? HttpsURLConnection)
            try {
                connection?.run {
                    readTimeout = 3000
                    connectTimeout = 3000
                    requestMethod = "GET"
                    doInput = true

                    //
                    if (responseCode != HttpsURLConnection.HTTP_OK) {
                        Log.e(TAG, "HTTP error code: $responseCode")
                        articleList.postValue(emptyList())
                        articles.value = emptyList()
                    }

                    //
                    inputStream?.let { stream ->
                        //val fetchedArticles = mutableListOf<ArticleData>()
                        val reader = JsonReader(BufferedReader(InputStreamReader(stream)))

                        //
                        /*reader.beginObject()
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
                                                                        "main" -> articleTitle =
                                                                            reader.nextString()

                                                                        else -> reader.skipValue()
                                                                    }
                                                                }
                                                                reader.endObject()
                                                            }

                                                            "web_url" -> {
                                                                articleUrl = reader.nextString()
                                                                if (articleTitle != null && articleUrl != null) {
                                                                    fetchedArticles.add(
                                                                        ArticleData(
                                                                            articleTitle,
                                                                            articleUrl
                                                                        )
                                                                    )
                                                                } else {
                                                                    Log.e(
                                                                        TAG,
                                                                        "Article missing title or url: $articleTitle, $articleUrl"
                                                                    )
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
                        reader.endObject()*/
                        //

                        val fetchedArticles = testFun(reader)

                        articleList.postValue(fetchedArticles)
                        articles.value = fetchedArticles
                    }
                }
            } finally {
                connection?.disconnect()
            }
        }
    }

    fun testFun(reader: JsonReader): MutableList<ArticleData> {
        val fetchedArticles = mutableListOf<ArticleData>()
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
                                var articleUrl: String? = null
                                while (reader.hasNext()) {
                                    reader.beginObject()
                                    while (reader.hasNext()) {
                                        when (reader.nextName()) {
                                            "web_url" -> {
                                                articleUrl = reader.nextString()
                                            }
                                            "headline" -> {
                                                reader.beginObject()
                                                while (reader.hasNext()) {
                                                    when (reader.nextName()) {
                                                        "main" -> {
                                                            articleTitle = reader.nextString()
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

                                    if (articleTitle != null && articleUrl != null) {
                                        fetchedArticles.add(
                                            ArticleData(
                                                articleTitle,
                                                articleUrl
                                            )
                                        )
                                    } else {
                                        Log.e(
                                            TAG,
                                            "Article missing title or url: $articleTitle, $articleUrl"
                                        )
                                    }

                                    articleTitle = null
                                    articleUrl = null
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

        return fetchedArticles
    }

}

data class ArticleState(
  val isLoading: Boolean = false,
    val articles: List<ArticleData> = emptyList()
  )