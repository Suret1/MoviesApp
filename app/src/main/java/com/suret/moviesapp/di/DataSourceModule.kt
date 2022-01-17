package com.suret.moviesapp.di

import com.suret.moviesapp.data.api.API
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.repository.datasource.LocalDataSource
import com.suret.moviesapp.data.repository.datasource.RemoteDataSource
import com.suret.moviesapp.data.repository.datasourceimpl.LocalDataSourceImpl
import com.suret.moviesapp.data.repository.datasourceimpl.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun providesLocalDataSource(movieDao: MovieDao): LocalDataSource {
        return LocalDataSourceImpl(movieDao)
    }

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        api: API
    ): RemoteDataSource {
        return RemoteDataSourceImpl(
            api
        )
    }
}