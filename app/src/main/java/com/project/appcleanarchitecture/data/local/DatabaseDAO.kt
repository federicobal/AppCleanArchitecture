package com.project.appcleanarchitecture.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.project.appcleanarchitecture.data.model.Point

/**
 * Created by Federico Bal on 6/5/2022.
 */
@Dao
interface DatabaseDAO {

    //Point
    @Insert
    fun pointInsert(point:Point)

    @Query("SELECT * FROM Point ORDER BY id DESC")
    fun pointGetAll(): List<Point>

    @Query("SELECT * FROM Point WHERE id=:id ORDER BY id DESC")
    fun pointGetById(id:String): Point

    @Query("DELETE FROM Point WHERE id=:id")
    fun pointDeleteById(id:String)

    @Query("DELETE FROM Point")
    fun pointDeleteAll()

}