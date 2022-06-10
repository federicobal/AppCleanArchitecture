package com.project.appcleanarchitecture.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.appcleanarchitecture.data.model.Point

/**
 * Created by fbal on 29/4/2022.
 */
@Database(entities = [Point::class], version = 0)

abstract class DatabaseLocal: RoomDatabase() {
    abstract fun databaseDAO(): DatabaseDAO

    companion object {
        private val nameRoomDataBase = "DatabaseLocalPoints"

        fun getDatabase(context: Context): DatabaseLocal{
            try {
                Companion.context = context
            }catch (ex:Exception){
                Log.e("sv_DatabaseLocal","${ex.message}")
            }
            return database
        }


        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private val database: DatabaseLocal by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            Room.databaseBuilder(context, DatabaseLocal::class.java, nameRoomDataBase)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }

   }
}