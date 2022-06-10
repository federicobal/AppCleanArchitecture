package com.project.appcleanarchitecture.data

import com.project.appcleanarchitecture.data.local.DatabaseDAO
import com.project.appcleanarchitecture.data.local.DatabaseLocal
import com.project.appcleanarchitecture.data.model.Point
import com.project.appcleanarchitecture.util.Resource
import javax.inject.Inject

class PointRepository @Inject constructor(private val pointDAO: DatabaseDAO):IPointRepository{
    override suspend fun getAllPoints(): Resource<List<Point>> {
        return Resource.Success(pointDAO.pointGetAll())
    }

    override suspend fun getPoint(id: String): Resource<Point> {
        return Resource.Success(pointDAO.pointGetById(id))
    }

    override suspend fun deletePoint(id: String): Resource<Boolean> {
        pointDAO.pointDeleteById(id)
        return  Resource.Success(true)
    }

    override suspend fun savePoint(point: Point): Resource<Boolean> {
        pointDAO.pointInsert(point)
        return Resource.Success(true)
    }
}