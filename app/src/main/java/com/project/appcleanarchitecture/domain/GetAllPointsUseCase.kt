package com.project.appcleanarchitecture.domain

import com.project.appcleanarchitecture.data.PointRepository
import com.project.appcleanarchitecture.data.model.Point
import com.project.appcleanarchitecture.util.Resource
import javax.inject.Inject

class GetAllPointsUseCase @Inject constructor(
        private val pointRepository: PointRepository){

    suspend operator fun invoke(): Resource<List<Point>> {
        return pointRepository.getAllPoints()
    }
}