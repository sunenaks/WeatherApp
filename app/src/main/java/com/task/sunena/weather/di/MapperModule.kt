package com.task.sunena.weather.di

import com.task.sunena.weather.domain.mapper.WeatherDataMapper
import com.task.sunena.weather.domain.mapper.WeatherDataMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Binds
    @Singleton
    abstract fun bindWeatherDataMapper(
        weatherDataMapperImpl: WeatherDataMapperImpl
    ): WeatherDataMapper
}