package com.delavar.arch.data.source.cloud

import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.data.model.ResponseModel
import io.reactivex.Flowable

interface CloudPlaceSource {
    /**
     * Attempt to explore places near to given position that is recommended
     */
    fun getPlaceList(latLng: String, limit: Int, offset: Int): Flowable<ResponseModel<PlaceListData>>
}