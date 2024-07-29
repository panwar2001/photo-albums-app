package com.example.photo_alumbs_app.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.example.photo_alumbs_app.Album
import com.example.photo_alumbs_app.data.source.local.AlbumEntity
import com.example.photo_alumbs_app.data.source.local.AppDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface AppInterface {
 suspend fun createAlbum(imageUris: List<Uri>)
 fun getAllAlbums(): Flow<List<Album>>
 fun getAllImages(id:String): List<Bitmap>
 suspend fun delete(id: String)
}

@Singleton
class AppRepo @Inject
constructor(@ApplicationContext private val context: Context,
    private val appDao: AppDao):AppInterface{
    override suspend fun createAlbum(
        imageUris:List<Uri>
    ) {
        val time=System.currentTimeMillis()
        val id= UUID.randomUUID().toString()
        val album = File(context.filesDir, "album_$id")
        if (!album.exists()) {
            album.mkdir()
        }
        context.contentResolver.apply {
            imageUris.forEach {
                Log.e("file size: ",it.toFile().length().toString())
                val name=UUID.randomUUID().toString()
                val newImageFile=File(album,"$name.jpeg")
                openInputStream(it)?.use { inputStream->
                    FileOutputStream(newImageFile).use { outputStream ->
                        outputStream.write(it.toFile().readBytes())
//                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
        appDao.createAlbum(
            AlbumEntity(
            id=id,
            date = time,
            albumName = toTimeDateString()
            )
        )
    }
    private fun toTimeDateString(): String {
        val time= Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss",Locale.getDefault())
        return sdf.format(time)
    }

    override fun getAllAlbums(): Flow<List<Album>> {
        return appDao.retrieveAllAlbums().map {album->
            album.map {
                Album(id=it.id,
                    thumbnail =getUri(it.id) ,
                    date = it.date,
                    albumName = it.albumName)
            }
        }
    }

    override fun getAllImages(id: String): List<Bitmap> {
        val album = File(context.filesDir, "album_$id")
        val files= album.listFiles() ?: return listOf()
        return files.map {
            FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                it
            ).toBitmap()
        }
    }
    private fun getUri(id:String):Bitmap{
        val album = File(context.filesDir, "album_$id")
        val files= album.listFiles() ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val uri= FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            files[0]
        )
        return uri.toBitmap()
    }
    private fun Uri.toBitmap(): Bitmap {
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(this)
            return BitmapFactory.decodeStream(inputStream)
                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a fallback bitmap if decoding fails
        } catch (e: Exception) {
            e.printStackTrace()
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Return a fallback bitmap on any exception
        } finally {
            inputStream?.close()
        }
    }

    override suspend fun delete(id: String) {
        val album = File(context.filesDir, "album_$id")
        album.delete()
        appDao.deleteAlbum(id)
    }
}