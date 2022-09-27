package com.bell.exercise.di

import android.app.Application
import android.content.Context
import com.bell.exercise.data.repository.CarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideMovieRepository(
        context: Context
    ): CarRepository {
        return CarRepository(
            context,
        )
    }


    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

}