package com.example.photo_alumbs_app.data.source.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Insert
    suspend fun createAlbum(album : AlbumEntity): Long

    @Query("select * from albums")
    fun retrieveAllAlbums(): Flow<List<AlbumEntity>>

    @Query("delete from albums where id=:id")
    suspend fun deleteAlbum(id:String)
}