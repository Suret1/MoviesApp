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
    fun provideGetTrendingMoviesAPI(retrofit: Retrofit): GetTrendingMoviesAPI {
        return retrofit.create(GetTrendingMoviesAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetCreditsAPI(retrofit: Retrofit): GetCreditsAPI {
        return retrofit.create(GetCreditsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetPersonDataAPI(retrofit: Retrofit): GetPersonDataAPI {
        return retrofit.create(GetPersonDataAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetMovieTrailerAPI(retrofit: Retrofit): GetMovieTrailerAPI {
        return retrofit.create(GetMovieTrailerAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsAPI(retrofit: Retrofit): GetMovieDetailsAPI {
        return retrofit.create(GetMovieDetailsAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetReviewsAPI(retrofit: Retrofit): GetReviewsAPI {
        return retrofit.create(GetReviewsAPI::class.java)
    }
    @Provides
    @Singleton
    fun provideGetSimilarAPI(retrofit: Retrofit): GetSimilarAPI {
        return retrofit.create(GetSimilarAPI::class.java)
    }
}