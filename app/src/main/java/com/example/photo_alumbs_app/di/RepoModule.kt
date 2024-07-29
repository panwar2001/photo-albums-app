package com.example.photo_alumbs_app.di

import com.example.photo_alumbs_app.data.AppInterface
import com.example.photo_alumbs_app.data.AppRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class  RepoModule {

    @Singleton
    @Binds
    abstract fun bindAppRepo(repository: AppRepo):AppInterface
}