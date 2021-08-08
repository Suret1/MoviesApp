package com.suret.moviesapp.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.suret.moviesapp.data.db.MovieDatabase
import com.suret.moviesapp.data.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MovieDatabase::class.java,
        Constants.DB_NAME
    ).allowMainThreadQueries().addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideMovieDao(
        database: MovieDatabase
    ) = database.movieDao()

    private val MIGRATION_1_2 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
//            with(database){
//                execSQL("ALTER TABLE movie_table ADD COLUMN testColumn")
//            }
        }
    }
}