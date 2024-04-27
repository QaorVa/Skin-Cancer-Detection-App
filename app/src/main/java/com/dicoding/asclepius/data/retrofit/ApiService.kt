package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.model.Article
import com.dicoding.asclepius.data.model.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("q") country: String = "Cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<ArticleResponse<ArrayList<Article>>>
}