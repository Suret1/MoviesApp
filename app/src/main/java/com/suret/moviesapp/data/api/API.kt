package com.suret.moviesapp.data.api

data class API(
    val getCreditsAPI: GetCreditsAPI,
    val getMovieDetailsAPI: GetMovieDetailsAPI,
    val getMovieTrailerAPI: GetMovieTrailerAPI,
    val getPersonDataAPI: GetPersonDataAPI,
    val getPersonMovieCreditsAPI: GetPersonMovieCreditsAPI,
    val getReviewsAPI: GetReviewsAPI,
    val getSimilarAPI: GetSimilarAPI,
    val getTrendingMoviesAPI: GetTrendingMoviesAPI
)
