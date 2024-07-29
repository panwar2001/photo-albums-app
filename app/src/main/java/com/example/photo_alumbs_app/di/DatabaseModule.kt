package com.example.photo_alumbs_app.di

import android.content.Context
import androidx.room.Room
import com.example.photo_alumbs_app.data.source.local.AppDao
import com.example.photo_alumbs_app.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "albums.db"
        ).build()
    }

    @Provides
    fun provideTextFileDao(database: AppDatabase): AppDao = database.appDao()
}
