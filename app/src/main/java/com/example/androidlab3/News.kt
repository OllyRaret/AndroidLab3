package com.example.androidlab3

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: List<Article>?
)

data class Article(
    val article_id: String,
    val title: String,
    val link: String,
    val source_id: String,
    val keywords: List<String>,
    val creator: Any?,
    val image_url: String?,
    val video_url: String?,
    val description: String?,
    val pubDate: String?,
    val content: String?,
    val country: Any?,
    val category: Any?,
    val language: String?
)
