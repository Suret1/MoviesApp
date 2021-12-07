package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.FilmographyRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPersonMovieCreditsAPI {

    @GET("/3/person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<FilmographyRoot>
}