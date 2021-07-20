package com.suret.moviesapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY vote_average DESC")
    fun getAllMovies(): LiveData<List<TrendingMoviesModel>>

    @Query("SELECT * FROM favorite_table ORDER BY vote_average DESC")
    fun getFavoriteMovies(): LiveData<List<FavoriteMovieModel>>

    @Query("DELETE FROM movie_table")
    suspend fun deleteMovieTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieList(movieModel: List<TrendingMoviesModel>)

    @Update
    suspend fun updateFavoriteStatus(movieModel: TrendingMoviesModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    @Delete
    suspend fun removeFavoriteMovie(favoriteMovieModel: FavoriteMovieModel)

    @Query("SELECT * FROM favorite_table WHERE id=:id")
    suspend fun getFavoriteMovieById(id: Int): FavoriteMovieModel

}
