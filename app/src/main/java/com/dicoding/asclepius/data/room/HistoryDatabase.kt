package com.dicoding.asclepius.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dicoding.asclepius.data.model.History
import com.dicoding.asclepius.data.model.UriConverter

@Database(
    entities = [History::class],
    version = 1
)
@TypeConverters(UriConverter::class)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryDatabase {
            if(INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        HistoryDatabase::class.java, "history_database")
                        .build()
                }
            }
            return INSTANCE as HistoryDatabase
        }
    }
}