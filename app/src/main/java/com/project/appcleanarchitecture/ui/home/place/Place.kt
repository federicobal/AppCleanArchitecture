package com.project.appcleanarchitecture.ui.home.place

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by fbal on 19/4/2022.
 */
data class Place(
    var id:String,
    var name: String,
    var latLng: LatLng,
    var address: String,
    var distance: Float,
    var rating: Float
) : ClusterItem {
    override fun getPosition(): LatLng =
        latLng

    override fun getTitle(): String =
        name

    override fun getSnippet(): String =
        address
}