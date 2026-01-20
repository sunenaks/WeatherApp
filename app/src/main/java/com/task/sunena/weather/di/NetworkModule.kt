package com.task.sunena.weather.di

import com.task.sunena.weather.data.remote.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.openweathermap.org/"

    /**
     * Provides a singleton instance of HttpLoggingInterceptor.
     * This is useful for debugging network requests, as it logs request and response bodies.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    /**
     * Provides a singleton instance of OkHttpClient.
     * The client includes the logging interceptor for debugging.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @Provides
   fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
       Retrofit.Builder()
           .baseUrl(BASE_URL)
           .client(okHttpClient)
           .addConverterFactory(GsonConverterFactory.create())
           .build()

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)


}