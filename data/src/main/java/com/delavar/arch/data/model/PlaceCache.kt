package com.delavar.arch.data.model

import com.delavar.arch.domain.model.Place

data class PlaceCache(
    val latLng: String,
    val offset: Int,
    val places: List<Place>
)