package com.myfitnesspal.nyt

import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    val articles: Flow<List<ArticleData>>

    suspend fun fetchArticles(): List<ArticleData>
}