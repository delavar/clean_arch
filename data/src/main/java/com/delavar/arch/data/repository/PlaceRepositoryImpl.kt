package com.delavar.arch.data.repository

import com.delavar.arch.data.source.cache.CachePlaceSource
import com.delavar.arch.data.source.cloud.CloudPlaceSource
import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.domain.repository.PlaceRepository
import com.delavar.arch.data.model.ResponseModel
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepositoryImpl @Inject constructor(
    private val cloudSource: CloudPlaceSource,
    private val cacheSource: CachePlaceSource
    ) : PlaceRepository {

    override fun getPlaceList(latLng: String, limit: Int, offset: Int): Flowable<List<Place>> {
        val places = cacheSource.getPlaceList(latLng, offset)
        if (places.isNotEmpty())
            return Flowable.just(places)
        else
            return cloudSource.getPlaceList(latLng, limit, offset).map {
                mapResponseAndCacheIt(it, latLng, offset)
            }
    }

    private fun mapResponseAndCacheIt(result: ResponseModel<PlaceListData>, latLng: String, offset: Int): List<Place> {
        return result.response.groups[0].items.map { it.venue }.also {
            cacheSource.storePlaceList(latLng, offset, it)
        }
    }
}

