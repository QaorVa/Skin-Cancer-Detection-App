package com.dicoding.asclepius.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.model.History

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(users: History)

    @Query("SELECT * FROM History")
    fun getAllHistories(): LiveData<List<History>>

    @Query("SELECT * FROM History WHERE id = :id")
    suspend fun getDetailHistory(id: Int): History

    @Query("DELETE FROM History WHERE id = :id")
    suspend fun deleteHistory(id: Int): Int
}