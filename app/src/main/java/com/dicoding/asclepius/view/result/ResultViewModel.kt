package com.dicoding.asclepius.view.result

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.model.Article
import com.dicoding.asclepius.data.model.ArticleResponse
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.data.retrofit.ApiConfig
import com.dicoding.asclepius.data.room.HistoryDao
import com.dicoding.asclepius.data.room.HistoryDatabase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultViewModel(application: Application): AndroidViewModel(application) {
    val listArticle = MutableLiveData<ArrayList<Article>?>()

    private var historyDao: HistoryDao?
    private var historyDatabase: HistoryDatabase?

    init {
        historyDatabase = HistoryDatabase.getDatabase(application)
        historyDao = historyDatabase?.historyDao()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    fun setArticles() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getTopHeadlines()
        client.enqueue(object: Callback<ArticleResponse<ArrayList<Article>>> {
            override fun onResponse(
                call: Call<ArticleResponse<ArrayList<Article>>>,
                response: Response<ArticleResponse<ArrayList<Article>>>
            ) {
                _isLoading.value = false
                if(response.isSuccessful) {
                    Log.d("Success", "Success")
                    _isError.value = false
                    listArticle.postValue(response.body()?.articles)
                }
            }

            override fun onFailure(call: Call<ArticleResponse<ArrayList<Article>>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
                Log.e("Failure", t.message.toString())
            }

        })
    }

    fun getArticles(): MutableLiveData<ArrayList<Article>?> {
        return listArticle
    }

    fun addHistory(label: String, confidence: String, imageUri: Uri?) {
        viewModelScope.launch {
            val history = History(
                id = 0,
                label = label,
                confidence = confidence,
                imageUri = imageUri
            )
            historyDao?.insertHistory(history)
        }
    }
}