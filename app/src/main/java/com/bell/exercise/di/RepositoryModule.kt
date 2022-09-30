package com.bell.exercise.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bell.exercise.core.Constants.Companion.CAR_TABLE
import com.bell.exercise.data.db.CarDao
import com.bell.exercise.data.repository.CarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.bell.exercise.data.db.CarDb
import javax.inject.Singleton

/**
 * RepositoryModule class is used to inject the dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * provideCarDb method will initialize the room db
     * @param context : used to access the resources
     */
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
     * @param carDao : used to interact with the data in Room DB
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

    /**
     * provideCarDb method will provide the DAO object
     * @param carDb : appDatabase class
     */
    @Provides
    fun provideCarDao(
        carDb: CarDb
    ) = carDb.carDao()


    /**
     * Provides the context in CarRepository
     * @param application : used to access the resources
     */
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

}