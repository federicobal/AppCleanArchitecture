package com.project.appcleanarchitecture.data

import com.project.appcleanarchitecture.data.local.DatabaseLocal
import com.project.appcleanarchitecture.data.model.Point
import com.project.appcleanarchitecture.util.Resource

interface IPointRepository{
    suspend fun getAllPoints():Resource<List<Point>>
    suspend fun getPoint(id:String):Resource<Point>
    suspend fun deletePoint(id:String):Resource<Boolean>
    suspend fun savePoint(point: Point):Resource<Boolean>
}