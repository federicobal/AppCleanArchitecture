package com.project.appcleanarchitecture.domain

import com.google.android.gms.maps.model.LatLng
import com.project.appcleanarchitecture.data.PointRepository
import com.project.appcleanarchitecture.data.model.Point
import com.project.appcleanarchitecture.ui.home.place.Place
import com.project.appcleanarchitecture.util.Resource
import javax.inject.Inject

class GetAllPointsUseCase @Inject constructor(
        private val pointRepository: PointRepository
        ){
    suspend operator fun invoke(): MutableList<Place> {
        var list: MutableList<Place> = mutableListOf()
        for (point:Point in pointRepository.getAllPoints()){
            list.add(Place(
                point.id,
                point.name,
                LatLng(point.latitud,point.langitude),
                point.address,
                point.distance,
                point.rating
            ))
        }
        return list
    }
}