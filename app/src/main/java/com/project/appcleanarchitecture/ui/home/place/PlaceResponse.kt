package com.project.appcleanarchitecture.ui.home.place

import com.google.android.gms.maps.model.LatLng

/**
 * Created by fbal on 19/4/2022.
 */
data class PlaceResponse(
    val id:String,
    val geometry: Geometry,
    val name: String,
    val vicinity: String,
    val rating: Float,
    val distance: Float
) {

    data class Geometry(
        val location: GeometryLocation
    )

    data class GeometryLocation(
        val lat: Double,
        val lng: Double
    )
}

fun PlaceResponse.toPlace(): Place = Place(
    id = id,
    name = name,
    latLng = LatLng(geometry.location.lat, geometry.location.lng),
    address = vicinity,
    rating = rating,
    distance = distance
)