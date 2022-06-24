package com.project.appcleanarchitecture.data

import com.project.appcleanarchitecture.data.model.Point

interface IPointRepository{
    suspend fun getAllPoints():List<Point>
    suspend fun getPoint(id:String):Point
    suspend fun deletePoint(id:String):Boolean
    suspend fun savePoint(point: Point):Boolean
}