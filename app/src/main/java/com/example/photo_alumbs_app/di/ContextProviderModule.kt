package com.example.photo_alumbs_app.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class) // Use ApplicationComponent for Application-scoped bindings
object ContextProviderModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}

