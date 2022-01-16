package com.suret.moviesapp.di

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
    fun provideUseCases(movieRepository: MovieRepository): UseCases {
        return UseCases(
            DeleteMoviesUseCase(movieRepository = movieRepository),
            GetAllMoviesUseCase(movieRepository = movieRepository),
            GetCreditsUseCase(movieRepository = movieRepository),
            GetFavoriteMovieByIdUseCase(movieRepository = movieRepository),
            GetFavoriteMoviesUseCase(movieRepository = movieRepository),
            GetMovieDetailsUseCase(movieRepository = movieRepository),
            GetMovieTrailerUseCase(movieRepository = movieRepository),
            GetPersonDataUseCase(movieRepository = movieRepository),
            GetPersonMovieCreditsUseCase(movieRepository = movieRepository),
            GetReviewsUseCase(movieRepository = movieRepository),
            GetSimilarMoviesUseCase(movieRepository = movieRepository),
            GetTrendingMoviesUseCase(movieRepository = movieRepository),
            InsertFavoriteMovieUseCase(movieRepository = movieRepository),
            InsertMoviesListUseCase(movieRepository = movieRepository),
            RemoteFavoriteMovieUseCase(movieRepository = movieRepository),
            UpdateFavoriteStatusUseCase(movieRepository = movieRepository)
        )
    }
}