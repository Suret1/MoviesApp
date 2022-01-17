package com.suret.moviesapp.di

import com.suret.moviesapp.data.repository.MovieRepositoryImpl
import com.suret.moviesapp.data.repository.datasource.LocalDataSource
import com.suret.moviesapp.data.repository.datasource.RemoteDataSource
import com.suret.moviesapp.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): MovieRepository {
        return MovieRepositoryImpl(localDataSource, remoteDataSource)
    }
}