package com.suret.moviesapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.suret.moviesapp.data.model.TrendingMoviesModel

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieModel: List<TrendingMoviesModel>)

    @Query("SELECT * FROM movie_table")
    fun getAllMovies(): List<TrendingMoviesModel>


}
