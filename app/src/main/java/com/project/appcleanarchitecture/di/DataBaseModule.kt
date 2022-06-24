package com.project.appcleanarchitecture.di

import android.content.Context
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
    /**
     * De esta manera comparte la base de datos a tod.o el proyecto
     */
    @Singleton
    @Provides
    fun provideDatabaseLocal(@ApplicationContext context: Context): DatabaseLocal {
        return DatabaseLocal.getInstance(context)
    }

    @Provides
    fun provideBasebaseDAO(databaseLocal: DatabaseLocal): DatabaseDAO {
        return databaseLocal.databaseDAO()
    }


}