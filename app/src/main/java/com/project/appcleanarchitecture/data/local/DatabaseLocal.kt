package com.project.appcleanarchitecture.data.local

import android.content.Context
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
        // For Singleton instantiation
        @Volatile private var instance: DatabaseLocal? = null

        fun getInstance(context: Context): DatabaseLocal {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): DatabaseLocal {
            return Room.databaseBuilder(context, DatabaseLocal::class.java, nameRoomDataBase)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
//            Room.databaseBuilder(context, DatabaseLocal::class.java, nameRoomDataBase)
//                .addCallback(
//                    object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
//                                .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
//                                .build()
//                            WorkManager.getInstance(context).enqueue(request)
//                        }
//                    }
//                )
//                .build()
        }
    }


//    companion object {
//
//        @SuppressLint("StaticFieldLeak")
//        private lateinit var context: Context
//
//        private val nameRoomDataBase = "DatabaseLocalPoints"
//
//        private val database: DatabaseLocal by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
//            Room.databaseBuilder(context, DatabaseLocal::class.java, nameRoomDataBase)
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build()
//        }
//
//        fun getDatabase(cont: Context): DatabaseLocal{
//            try {
//                Companion.context = cont
//            }catch (ex:Exception){
//                Log.e("sv_DatabaseLocal","${ex.message}")
//            }
//            return database
//        }
//
//
//   }
}