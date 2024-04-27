package com.dicoding.asclepius.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var label: String,
    var confidence: String,
    @TypeConverters(UriConverter::class)
    var imageUri: Uri?
)

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}