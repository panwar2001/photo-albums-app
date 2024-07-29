package com.example.photo_alumbs_app.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: String,
    val albumName: String="",
    val date: Long
)