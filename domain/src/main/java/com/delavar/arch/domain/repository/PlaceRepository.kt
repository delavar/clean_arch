package com.delavar.arch.domain.repository

import com.delavar.arch.domain.model.Place
import io.reactivex.Flowable

interface PlaceRepository {
    fun getPlaceList(latLng: String, limit: Int, offset: Int): Flowable<List<Place>>
}