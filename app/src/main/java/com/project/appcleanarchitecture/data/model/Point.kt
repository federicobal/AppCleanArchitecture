package com.project.appcleanarchitecture.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by fbal on 29/4/2022.
 */
@Entity(tableName = "Point")
data class Point(@PrimaryKey @ColumnInfo(name = "id")
                 var id:String,
                 var name:String,
                 var latitud:Double,
                 var langitude: Double,
                 var address:String,
                 var distance: Float,
                 var rating: Float)
