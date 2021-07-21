package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.CreditsModelRoot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetCreditsAPI {

    @GET("/3/movie/{id}/credits")
    suspend fun getCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsModelRoot>

}