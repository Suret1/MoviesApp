package com.suret.moviesapp.data.api

import com.suret.moviesapp.data.model.ReviewModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetReviewsAPI {

    @GET("/3/movie/{id}/reviews")
    suspend fun getReviews(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<ReviewModel>

}