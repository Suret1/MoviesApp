package com.suret.moviesapp.data.di

import android.content.Context
import androidx.room.Room
import com.suret.moviesapp.BuildConfig
import com.suret.moviesapp.data.api.IAPI
import com.suret.moviesapp.data.db.MovieDao
import com.suret.moviesapp.data.db.MovieDatabase
import com.suret.moviesapp.data.domain.MovieRepository
import com.suret.moviesapp.data.domain.MovieRepositoryImpl
import com.suret.moviesapp.data.other.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MovieDatabase::class.java,
        DB_NAME
    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideMovieDao(
        database: MovieDatabase
    ) = database.movieDao()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(client: OkHttpClient): IAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
            .create(IAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: IAPI,
        movieDao: MovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(api,movieDao)
    }


}