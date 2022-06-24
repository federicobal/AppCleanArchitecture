package com.project.appcleanarchitecture.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.clustering.ClusterManager
import com.project.appcleanarchitecture.Application
import com.project.appcleanarchitecture.R
import com.project.appcleanarchitecture.databinding.FragmentHomeBinding
import com.project.appcleanarchitecture.ui.home.place.Place
import com.project.appcleanarchitecture.ui.home.place.PlaceRenderer
import com.project.appcleanarchitecture.ui.home.place.PlacesReader
import com.project.appcleanarchitecture.util.StatusEnum
import com.project.appcleanarchitecture.util.hide
import com.project.appcleanarchitecture.util.show
import com.google.android.gms.maps.model.Marker
import com.project.appcleanarchitecture.util.LoadingEnum
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    //private lateinit var homeViewModel: HomeViewModel
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var id:String=""

//    private val places: List<Place> by lazy {
//        PlacesReader(Application.applicationContext()).read()
//    }
    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val root: View = binding.root
        initView()
        Load()
        return root
    }
    private fun initView()
    {
        var tZoneName:TextView= binding.tZoneName
        var etZoneName:EditText =binding.etZoneName
        var tDistance:TextView=binding.tDistance
        var etDistance:EditText=binding.etDistance
        var sbDistance:SeekBar=binding.sbDistance
        var btnSave:Button=binding.btnSave
        var btnDelete:Button=binding.btnDelete
        var pbProgress:ProgressBar=binding.progress

        homeViewModel.loading.observe(viewLifecycleOwner,{
            when(it){
                LoadingEnum.DONE->{
                    pbProgress.hide()
                    Log.d(TAG, "Done") }
                LoadingEnum.FAIL-> Log.d(TAG,"Fail")
                LoadingEnum.LOADING-> {
                    Log.d(TAG, "loading")
                    pbProgress.show()
                }
                else -> {
                    pbProgress.hide()
                }
            }
        })
        homeViewModel.status.observe(viewLifecycleOwner,{
            when(it){
                StatusEnum.NEW -> {
                    Log.d(TAG,"NEW")
                    btnDelete.hide()
                    btnSave.show()
                }
                StatusEnum.EDIT -> {
                    Log.d(TAG,"EDIT")
                    btnDelete.show()
                    btnSave.show()
                }else -> {
                }
            }
        })
        homeViewModel.place.observe(viewLifecycleOwner, Observer{
            it.let {
                binding.apply {
                    id=it.id
                    etZoneName.setText(it.address)
                    etDistance.setText(it.distance.toString())
                    sbDistance.progress=it.distance.toInt()
                }
            }
        })
        homeViewModel.listAddressPosition.observe(viewLifecycleOwner, Observer{

            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            //val mapFragment = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                // Ensure all places are visible in the map
//                googleMap.setOnMapLoadedCallback {
//                    val bounds = LatLngBounds.builder()
//                    it.forEach { bounds.include(it.latLng) }
//                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
//                }

                addMarkers(googleMap,it)
//                addClusteredMarkers(googleMap,it)

                // Set custom info window adapter
                // googleMap.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
            }
        })
        btnSave.setOnClickListener {
            //etZoneName.text
            //homeViewModel.distance= etDistance.value
            binding.apply {
                homeViewModel.savePoint(id,0.0,0.0,"",
                    etZoneName.getText().toString(),
                    sbDistance.progress.toFloat())
            }
        }
        btnDelete.setOnClickListener {
            homeViewModel.deletePoint(id)
        }
    }
    /**
     * Adds markers to the map. These markers won't be clustered.
     */
    @SuppressLint("PotentialBehaviorOverride")
    private fun addMarkers(googleMap: GoogleMap, places:List<Place>) {
        googleMap.clear()
        places.forEach { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
                    .icon(bicycleIcon)

            )

            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            marker!!.tag = place
        }
        googleMap.setOnMarkerClickListener { marker -> // Triggered when user click any marker on the map
            Log.d("dd","title:"+ marker.title.toString()+
                    " id:"+marker.id+" More:" + marker.tag.toString())
            //homeViewModel.getPoint((marker.tag as Place).id)
            false
        }
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap,places: List<Place>) {
        // Create the ClusterManager class and set the custom renderer
        val clusterManager = ClusterManager<Place>(Application.applicationContext(), googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                Application.applicationContext(),
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(Application.applicationContext()))

        // Add the places to the ClusterManager
        clusterManager.addItems(places)
        clusterManager.cluster()

        // Show polygon
        clusterManager.setOnClusterItemClickListener { item ->
            addCircle(googleMap, item)
            return@setOnClusterItemClickListener false
        }

        // When the camera starts moving, change the alpha value of the marker to translucent
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }

        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
            // can be performed when the camera stops moving
            clusterManager.onCameraIdle()
        }
    }

    private var circle: Circle? = null

    /**
     * Adds a [Circle] around the provided [item]
     */
    private fun addCircle(googleMap: GoogleMap, item: Place) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(item.latLng)
                .radius(1000.0)
                .fillColor(ContextCompat.getColor(Application.applicationContext(), R.color.colorPrimaryTranslucent))
                .strokeColor(ContextCompat.getColor(Application.applicationContext(), R.color.colorPrimary))
        )
    }

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(Application.applicationContext(), R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(Application.applicationContext(), R.drawable.ic_directions_bike_black_24dp, color)
    }



    private fun Load(){
        homeViewModel.loadPositions()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}