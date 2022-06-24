package com.project.appcleanarchitecture.data

import com.project.appcleanarchitecture.data.local.DatabaseDAO
import com.project.appcleanarchitecture.data.model.Point
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointRepository @Inject constructor(private val databaseDAO: DatabaseDAO):IPointRepository{
    override suspend fun getAllPoints(): List<Point> {
        return databaseDAO.pointGetAll()
    }

    override suspend fun getPoint(id: String): Point {
        return databaseDAO.pointGetById(id)
    }

    override suspend fun deletePoint(id: String): Boolean {
        databaseDAO.pointDeleteById(id)
        return true
    }

    override suspend fun savePoint(point: Point): Boolean {
        databaseDAO.pointInsert(point)
        return true
    }
}