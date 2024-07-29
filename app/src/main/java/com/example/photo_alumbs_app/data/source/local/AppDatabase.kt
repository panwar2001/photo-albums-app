package com.example.photo_alumbs_app.data.source.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AlbumEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract  fun appDao(): AppDao
}