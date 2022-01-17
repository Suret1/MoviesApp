package com.suret.moviesapp.di

import com.suret.moviesapp.BuildConfig
import com.suret.moviesapp.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetModule {

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
    fun provideMovieApi(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): API {
        return API(
            getCreditsAPI = retrofit.create(GetCreditsAPI::class.java),
            getMovieDetailsAPI = retrofit.create(GetMovieDetailsAPI::class.java),
            getMovieTrailerAPI = retrofit.create(GetMovieTrailerAPI::class.java),
            getPersonDataAPI = retrofit.create(GetPersonDataAPI::class.java),
            getPersonMovieCreditsAPI = retrofit.create(GetPersonMovieCreditsAPI::class.java),
            getReviewsAPI = retrofit.create(GetReviewsAPI::class.java),
            getSimilarAPI = retrofit.create(GetSimilarAPI::class.java),
            getTrendingMoviesAPI = retrofit.create(GetTrendingMoviesAPI::class.java)
        )
    }
}