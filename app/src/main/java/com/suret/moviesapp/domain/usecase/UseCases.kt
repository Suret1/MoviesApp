package com.suret.moviesapp.domain.usecase

data class UseCases(
    val deleteMoviesUseCase: DeleteMoviesUseCase,
    val getAllMoviesUseCase: GetAllMoviesUseCase,
    val getCreditsUseCase: GetCreditsUseCase,
    val getFavoriteMovieByIdUseCase: GetFavoriteMovieByIdUseCase,
    val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    val getMovieTrailerUseCase: GetMovieTrailerUseCase,
    val getPersonDataUseCase: GetPersonDataUseCase,
    val getPersonMovieCreditsUseCase: GetPersonMovieCreditsUseCase,
    val getReviewsUseCase: GetReviewsUseCase,
    val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    val getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    val insertFavoriteMovieUseCase: InsertFavoriteMovieUseCase,
    val insertMoviesListUseCase: InsertMoviesListUseCase,
    val remoteFavoriteMovieUseCase: RemoteFavoriteMovieUseCase,
    val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
)
