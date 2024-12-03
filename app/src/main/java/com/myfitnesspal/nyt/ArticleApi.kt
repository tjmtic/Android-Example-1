package com.myfitnesspal.nyt

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("articlesearch.json")
    suspend fun getArticles( @Query("api-key") apiKey: String) : JsonObject
}