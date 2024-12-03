package com.myfitnesspal.nyt

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

public open class ArticleRepositoryImpl @Inject constructor(val articleApi: ArticleApi) : ArticleRepository {

    companion object {
        private const val API_KEY = "oFjnnSAIsADysrOAdAF5ihGlecFPNLU4"//BuildConfig.API_KEY
    }

    val _articles: MutableStateFlow<List<ArticleData>> = MutableStateFlow(emptyList())
    override val articles: Flow<List<ArticleData>> = _articles

    override suspend fun fetchArticles(): List<ArticleData> {
        val fetchedArticles = repoFetchArticles()
        println("Saving it:-REPOSITORY1 ${_articles.value}")
        _articles.value = fetchedArticles
        println("Saving it:-REPOSITORY2 ${_articles.value}")

        println("repo fetch articles ${fetchedArticles}")

        return fetchedArticles
    }

    private suspend fun repoFetchArticles(): List<ArticleData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = articleApi.getArticles(API_KEY)

                when (response["status"].asString){
                    "OK" -> {
                        println("REPO RESPONSE OK")
                        Mapper.parseResponseToList(response) ?: emptyList()
                    }
                    else -> {
                        println("REPO RESPONSE is not OK ${response["status"]} ${response["status"].asString == "\"OK\""}")
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                // Handle error
                emptyList()
            }
        }
    }

    /*internal fun parseResponseToList(response: JsonObject): List<ArticleData>?{
        return response["response"].asJsonObject?.getAsJsonArray("docs")?.mapNotNull { docs ->
                val r = docs.asJsonObject
                val title: String? = r["headline"]?.asJsonObject?.get("main")?.asString
                val url: String? = r["web_url"]?.asString

                return@mapNotNull when {
                    title?.isNotBlank() == true && url?.isNotBlank() == true -> {
                        ArticleData(title, url)
                    }
                    else -> {
                        null
                    }
                }
            }
    }*/
}