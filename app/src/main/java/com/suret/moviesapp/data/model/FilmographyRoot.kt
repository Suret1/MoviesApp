package com.suret.moviesapp.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FilmographyRoot(
    @SerializedName("cast")
    val filmography: List<Filmography>? = null,
    val id: Int? = null
)
@Keep
data class Filmography(
    val adult: Boolean? = null,
    val backdrop_path: String? = null,
    val character: String? = null,
    val credit_id: String? = null,
    val genre_ids: List<Int>? = null,
    val id: Int? = null,
    val order: Int? = null,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null
)