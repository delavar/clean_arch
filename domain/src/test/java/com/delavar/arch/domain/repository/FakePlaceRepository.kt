package com.delavar.arch.domain.repository

import com.delavar.arch.domain.model.Place
import io.reactivex.Flowable

class FakePlaceRepository : PlaceRepository {
    override fun getPlaceList(latLng: String, limit: Int, offset: Int): Flowable<List<Place>> {
        val place1=Place("1","yara company")
        val place2=Place("2","azadi")
        return Flowable.just(arrayListOf(place1,place2))
    }
}