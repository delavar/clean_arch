package com.delavar.arch.data.source.cloud

import com.delavar.arch.data.mapper.CloudErrorMapper
import com.delavar.arch.data.restful.ApiService
import com.delavar.arch.data.util.FoursquareKey
import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.data.model.ResponseModel
import io.reactivex.Flowable
import javax.inject.Inject

class CloudPlaceSourceImpl @Inject constructor(val apiService: ApiService,
                                               val keys: FoursquareKey,
                                               val errorMapper: CloudErrorMapper) : CloudPlaceSource {

    companion object {
        private const val API_VERSION = "20190525"
    }

    override fun getPlaceList(latLng: String, limit: Int, offset: Int): Flowable<ResponseModel<PlaceListData>> {
        return apiService.getPlaceList(
            latLng,
            limit,
            offset,
            keys.getClientID(),
            keys.getClientSecret(),
            API_VERSION
        ).onErrorResumeNext { t: Throwable -> Flowable.error(errorMapper.mapToErrorException(t)) }
    }
}