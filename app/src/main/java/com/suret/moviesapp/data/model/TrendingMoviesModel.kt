package com.suret.moviesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class TrendingMoviesModel(
    val backdrop_path: String? = null,
    val first_air_date: String? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = null,
    val original_language: String? = null,
    val original_name: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val release_date: String? = null,
    val title: String? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null
)