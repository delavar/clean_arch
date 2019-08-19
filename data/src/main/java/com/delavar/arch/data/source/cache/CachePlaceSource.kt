package com.delavar.arch.data.source.cache

import com.delavar.arch.domain.model.Place

interface CachePlaceSource {
    fun getPlaceList(latLng: String, offset: Int) : List<Place>
    fun storePlaceList(latLng: String, offset: Int,places:List<Place>)
}