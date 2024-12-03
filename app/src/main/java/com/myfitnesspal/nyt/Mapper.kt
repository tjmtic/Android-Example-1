package com.myfitnesspal.nyt

import com.google.gson.stream.JsonReader;
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object Mapper {

    fun parseResponse(response: JsonObject): List<ArticleData>? {
       return when (response["status"].asString){
            "OK" -> {
                println("REPO RESPONSE OK")
                parseResponseToList(response) ?: emptyList()
            }
            else -> {
                println("REPO RESPONSE is not OK ${response["status"]} ${response["status"].asString == "\"OK\""}")
                emptyList()
            }
        }
    }

    internal fun parseResponseToList(response: JsonObject): List<ArticleData>?{
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
    }


    internal fun streamParse(stream: InputStream?): List<ArticleData>? {
        val reader = JsonReader(BufferedReader(InputStreamReader(stream)))
        return testFun(reader)
    }

    internal fun testFun(reader: JsonReader): MutableList<ArticleData> {
        val fetchedArticles = mutableListOf<ArticleData>()
        reader.beginObject()
        var status: String
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "status" -> {
                    status = reader.nextString()
                   // Log.d("TAG Mapper", "status $status")
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
                                      //  Log.e(
                                      //      "TAG Mapper",
                                      //      "Article missing title or url: $articleTitle, $articleUrl"
                                      //  )
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