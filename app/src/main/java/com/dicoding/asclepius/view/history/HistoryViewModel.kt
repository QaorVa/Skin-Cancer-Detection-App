package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.data.room.HistoryDao
import com.dicoding.asclepius.data.room.HistoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application): AndroidViewModel(application) {

    private var historyDao: HistoryDao?
    private var historyDatabase: HistoryDatabase?

    init {
        historyDatabase = HistoryDatabase.getDatabase(application)
        historyDao = historyDatabase?.historyDao()
    }

    fun getAllHistory(): LiveData<List<History>>? {
        return historyDao?.getAllHistories()
    }

    fun deleteHistory(id: Int) {
        CoroutineScope((Dispatchers.IO)).launch {
            historyDao?.deleteHistory(id)
        }
    }

}