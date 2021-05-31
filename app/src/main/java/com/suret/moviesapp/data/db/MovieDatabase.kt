package com.suret.moviesapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suret.moviesapp.data.model.TrendingMoviesModel
import com.suret.moviesapp.util.GenreConverter

@Database(entities = [TrendingMoviesModel::class], version = 2, exportSchema = false)
@TypeConverters(GenreConverter::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}