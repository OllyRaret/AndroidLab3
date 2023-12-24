package com.example.androidlab3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("/api/1/news")
    suspend fun getNews(
        @Query("apikey") apiKey: String,
        @Query("q") query: String
    ): Response<NewsResponse>
}