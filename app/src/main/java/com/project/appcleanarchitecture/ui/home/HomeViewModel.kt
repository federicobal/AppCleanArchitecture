package com.project.appcleanarchitecture.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.project.appcleanarchitecture.data.model.Point
import com.project.appcleanarchitecture.domain.GetAllPointsUseCase
import com.project.appcleanarchitecture.ui.home.place.Place
import com.project.appcleanarchitecture.util.LoadingEnum
import com.project.appcleanarchitecture.util.Resource
import com.project.appcleanarchitecture.util.StatusEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.assertThreadDoesntHoldLock
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllPointsUseCase: GetAllPointsUseCase) :
    ViewModel() {

    private val maxDistance = 10

    private val _status = MutableLiveData<StatusEnum>().apply{
        value=StatusEnum.NEW
    }

    val status : LiveData<StatusEnum>
        get()=_status

    private val _loading : MutableLiveData<LoadingEnum> = MutableLiveData<LoadingEnum>().
    apply {
        value=LoadingEnum.LOADING
    }
    val loading : LiveData<LoadingEnum>
        get() =_loading

    private val _place = MutableLiveData<Place>().apply {
        value = Place("","New gps zone alarm",LatLng(0.0,0.0),"",5F,0F)
    }
    val place: LiveData<Place> = _place

    fun clear()
    {
        _place.value = Place("","",LatLng(0.0,0.0),"",0F,0F)
    }
    private val _listAddressPosition: MutableLiveData<MutableList<Place>>
    = MutableLiveData<MutableList<Place>>()

    val listAddressPosition: LiveData<MutableList<Place>>
        get()= _listAddressPosition

    fun loadPositions()
    {
        _loading.value = LoadingEnum.LOADING
        viewModelScope.launch {
            try {
                delay(10L * 1000L)
                var list: MutableList<Place> = mutableListOf()
                list.add(Place(list.count().toString(),"101-00001 - GÜEMES", LatLng(-34.491124,-58.845394),"ALBERDI 413",1.0F,0.0F))
//                list.add(Place(list.count().toString(),"AROCENA, MIRIAM RAQ", LatLng(-34.558332,-58.450981),"VIRREY ARREDONDO 03448 07B",2.0F,0.0F))
//                list.add(Place(list.count().toString(),"CRUZ MASCHIO PEDRO A", LatLng(-34.491228,-58.845317),"ARREDONDO 2553 G 15",3.0F,0.0F))
//                list.add(Place(list.count().toString(),"D.G.R. - DELEGACIÓN GENERAL GÜ", LatLng(-34.491131,-58.845374),"RIVADAVIA Y SAN MARTIN",4.0F,0.0F))
//                list.add(Place(list.count().toString(),"D.G.R. - DELEGACIÓN GENERAL GÜ", LatLng(-34.491141,-58.845383),"RIVADAVIA Y SAN MARTIN",5.0F,0.0F))
//                list.add(Place(list.count().toString(),"HOSPITAL J. CASTELLANO (GRAL G", LatLng(-34.491154,-58.845216),"CABRED S/N RUTA 34",6.0F,0.0F))
//                list.add(Place(list.count().toString(),"HTAL GUEMES NEONATOLOGIA", LatLng(-34.491154,-58.845216),"CABRED S/N RUTA 34",7.0F,0.0F))
//                list.add(Place(list.count().toString(),"JUAN CARLOS COSTILLA", LatLng(-34.491124,-58.845391),"LIBERTAD 302",8.0F,0.0F))
//                list.add(Place(list.count().toString(),"MACRO PREMIA", LatLng(-34.854783,-58.390487),"SARMIENTO 447",9.0F,0.0F))
//                list.add(Place(list.count().toString(),"MARCOS MANUEL VILLALBA", LatLng(-34.491124,-58.845391),"LIBERTAD 302",10.0F,0.0F))
//                list.add(Place(list.count().toString(),"MARIA MERCEDES MONGE", LatLng(-34.491124,-58.845391),"LIBERTAD 302",10.0F,0.0F))
//                list.add(Place(list.count().toString(),"PENA ANDREA CELESTE", LatLng(-34.558337,-58.450985),"ZAPIOLA 1759 4 C",11.0F,0.0F))
//                list.add(Place(list.count().toString(),"PUMA", LatLng(-34.854722,-58.390436),"25 DE MAYO 259",12.0F,0.0F))
//                list.add(Place(list.count().toString(),"VALERIA DEL CARMEN CARI", LatLng(-34.491228,-58.845317),"SAN LORENZO  551",13.0F,0.0F))

                _listAddressPosition.value=getAllPointsUseCase()!!
                _loading.value = LoadingEnum.DONE
            }catch (ex:Exception)
            {
                _loading.value = LoadingEnum.FAIL

            }
        }
    }
    fun getPoint(id:String)
    {
        _status.value = StatusEnum.EDIT
        _loading.value = LoadingEnum.LOADING
        var p: Place? = (_listAddressPosition.value as List<Place>).filter{ it.id == id}.firstOrNull()
        if(p != null)
        {
            if (p.distance.toInt()>maxDistance)
            {
                p.distance = 0F
            }
            _place.value = p
//            _id.value=id.toInt()
//            if (p.distance.toInt()>maxDistance) {
//                _distance.value = maxDistance
//            }
//            else {
//                _distance.value = p?.distance.toInt()
//            }
//            _address.value=p?.address
        }
        _loading.value = LoadingEnum.DONE
    }
    fun savePoint(id:String,
                  latitude:Double,
                  longitude: Double,
                  name: String,
                  address: String,
                  distance:Float)
    {
        _status.value = StatusEnum.NEW
        _loading.value = LoadingEnum.LOADING
        var p: Place? = (_listAddressPosition.value as List<Place>).filter{ it.id == id}.firstOrNull()
        if(p != null)
        {
            p.address = address
            p.distance = distance
            clear()
        }
        else
        {
            val pos:String=((_listAddressPosition.value?.count()?: 0 )+1).toString()
            val pp=Place( pos,name, LatLng(latitude,longitude),address,distance,0.0F)
            _listAddressPosition.value?.add(pp)

        }
////        val id:String = _id?.value?.toString() ?: "0"
//        (_listAddressPosition.value as List<Place>).filter{ it.id == id }.firstOrNull()
//            .let {
//                _id.value=0
//                //(_distance.value ?: 0)
//                if (distance > maxDistance) {
//                    it?.distance = maxDistance.toFloat()
//                }
//                else {
//                    it?.distance = distance.toFloat()//(_distance.value ?: 0)
//                }
//                it?.address = address //_address.value ?: ""
//            }
        _loading.value = LoadingEnum.DONE
    }
    fun deletePoint(id:String)
    {
        //(_id?.value ?: "")
        _listAddressPosition.value.let { l ->
            if( l !=null && (l.count())>0) {
                _listAddressPosition.value=
                    _listAddressPosition.value?.filter{ it.id != id.toString() }
                            as MutableList<Place>?
            }
        }
    }
}
