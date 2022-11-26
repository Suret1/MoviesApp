package com.suret.moviesapp.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suret.moviesapp.data.model.FavoriteMovieModel
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.GenreConverter

@Database(
    entities = [TrendingMoviesModel::class, FavoriteMovieModel::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from =  5, to = 6)
    ]
)
@TypeConverters(GenreConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}