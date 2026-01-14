package com.task.sunena.weather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

   @Provides
   fun provideRetrofit(): Retrofit =
       Retrofit.Builder()
           .baseUrl("https://openweathermap.org/")
           .addConverterFactory(GsonConverterFactory.create())
           .build()


}