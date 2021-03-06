package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.ActorModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPersonDataAPI {

    @GET("/3/person/{person_id}")
    suspend fun getPersonData(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<ActorModel>

}