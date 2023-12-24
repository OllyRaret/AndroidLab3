package com.example.androidlab3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var keywordEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var totalResultsTextView: TextView
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var newsApiService: NewsApiService
    private val apiKey = getString(R.string.apikey)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Создаем Retrofit-инстанс
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создаем объект для выполнения запросов к API
        newsApiService = retrofit.create(NewsApiService::class.java)

        // Получаем ссылки на элементы пользовательского интерфейса
        recyclerView = findViewById(R.id.recyclerView)
        keywordEditText = findViewById(R.id.keywordEditText)
        searchButton = findViewById(R.id.searchButton)
        totalResultsTextView = findViewById(R.id.totalResultsTextView)

        newsAdapter = NewsAdapter()
        recyclerView.adapter = newsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Обработчик нажатия на кнопку поиска
        searchButton.setOnClickListener {
            val query = keywordEditText.text.toString()

            // Выполняем запрос к API в фоновом потоке
            CoroutineScope(Dispatchers.Main).launch {
                fetchNews(apiKey, query)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun fetchNews(apiKey: String, query: String) {
        val response = newsApiService.getNews(apiKey, query)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val newsResponse = response.body()
                Log.d("API_RESPONSE", newsResponse.toString())

                // Обновляем количество результатов
                totalResultsTextView.text = "Total Results: ${newsResponse?.totalResults ?: 0}"

                // Обновляем RecyclerView
                newsResponse?.results?.let {
                    newsAdapter.submitList(it)
                }
            } else {
                Log.e("API_RESPONSE_ERROR", "Error: ${response.code()}, ${response.message()}")
            }
        }
    }
}