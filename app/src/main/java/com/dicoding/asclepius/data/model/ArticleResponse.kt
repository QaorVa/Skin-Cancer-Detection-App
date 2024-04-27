package com.dicoding.asclepius.data.model

data class ArticleResponse<out T>(
    val status: String?,
    val totalResults: Int?,
    val articles: T
)
