package com.project.appcleanarchitecture.di

import android.content.Context
import com.project.appcleanarchitecture.data.PointRepository
import com.project.appcleanarchitecture.data.local.DatabaseDAO
import com.project.appcleanarchitecture.data.local.DatabaseLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Federico Bal on 9/6/2022.
 */

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Singleton
    @Provides
    /**
     * De esta manera comparte la base de datos a tod.o el proyecto
     */
    fun provideAppDatabase(@ApplicationContext context: Context): DatabaseLocal {
        return DatabaseLocal.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBaseLocal(databaseDatabaseLocal: DatabaseLocal): DatabaseDAO {
        return databaseDatabaseLocal.databaseDAO()
    }

}