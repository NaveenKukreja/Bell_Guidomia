package com.bell.exercise.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bell.exercise.core.Constants.Companion.CAR_TABLE
import com.bell.exercise.data.network.CarDao
import com.bell.exercise.data.repository.CarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.bell.exercise.data.network.CarDb
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCarDb(
        @ApplicationContext
        context : Context
    ) = Room.databaseBuilder(
        context,
        CarDb::class.java,
        CAR_TABLE
    ).build()

    /**
     * Provides RemoteDataRepository for access api service method
     * @param context : used to access the resources
     */
    @Singleton
    @Provides
    fun provideCarRepository(
        context: Context,
        carDao: CarDao
    ): CarRepository {
        return CarRepository(
            context,carDao
        )
    }

    @Provides
    fun provideCarDao(
        bookDb: CarDb
    ) = bookDb.carDao()


    /**
     * Provides the context in CarRepository
     * @param application : used to access the resources
     */
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

}