package com.suret.moviesapp.data.di

import com.suret.moviesapp.domain.repository.MovieRepository
import com.suret.moviesapp.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideDeleteMoviesUseCase(movieRepository: MovieRepository): DeleteMoviesUseCase {
        return DeleteMoviesUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetAllMoviesUseCase(movieRepository: MovieRepository): GetAllMoviesUseCase {
        return GetAllMoviesUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetCreditsUseCase(movieRepository: MovieRepository): GetCreditsUseCase {
        return GetCreditsUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetFavoriteMovieByIdUseCase(movieRepository: MovieRepository): GetFavoriteMovieByIdUseCase {
        return GetFavoriteMovieByIdUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetFavoriteMoviesUseCase(movieRepository: MovieRepository): GetFavoriteMoviesUseCase {
        return GetFavoriteMoviesUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetMovieTrailerUseCase(movieRepository: MovieRepository): GetMovieTrailerUseCase {
        return GetMovieTrailerUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetPersonDataUseCase(movieRepository: MovieRepository): GetPersonDataUseCase {
        return GetPersonDataUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetTrendingMoviesUseCase(movieRepository: MovieRepository): GetTrendingMoviesUseCase {
        return GetTrendingMoviesUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideInsertFavoriteMovieUseCase(movieRepository: MovieRepository): InsertFavoriteMovieUseCase {
        return InsertFavoriteMovieUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideInsertMoviesListUseCase(movieRepository: MovieRepository): InsertMoviesListUseCase {
        return InsertMoviesListUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideRemoveFavoriteMovieUseCase(movieRepository: MovieRepository): RemoteFavoriteMovieUseCase {
        return RemoteFavoriteMovieUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideUpdateFavoriteStatusUseCase(movieRepository: MovieRepository): UpdateFavoriteStatusUseCase {
        return UpdateFavoriteStatusUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetMovieDetailsUseCase(movieRepository: MovieRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetReviewsUseCase(movieRepository: MovieRepository): GetReviewsUseCase {
        return GetReviewsUseCase(movieRepository)
    }
}