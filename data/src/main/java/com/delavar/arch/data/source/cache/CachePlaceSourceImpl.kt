package com.delavar.arch.data.source.cache

import com.delavar.arch.data.model.PlaceCache
import com.delavar.arch.data.util.CacheDelegate
import com.delavar.arch.data.util.LocationUtil
import com.delavar.arch.domain.model.Place

class CachePlaceSourceImpl constructor(
    private val locationUtil: LocationUtil,
    private val expirationCacheTimeMillis: Long = CacheDelegate.CACHE_FOR_20_MINUTES
) : CachePlaceSource {
    companion object {

        private const val MIN_DISTANCE = 100f
    }

    private var cachedExplorePlaces: MutableList<PlaceCache>? by CacheDelegate(expirationCacheTimeMillis)

    init {
        cachedExplorePlaces = mutableListOf()
    }

    override fun getPlaceList(latLng: String, offset: Int): List<Place> {
        // get cached places based on latLng and offset if exists
        val cachedPlaces = cachedExplorePlaces?.firstOrNull { isCached(it, latLng, offset) }
        return cachedPlaces?.let { it.places } ?: emptyList()
    }

    override fun storePlaceList(latLng: String, offset: Int, places: List<Place>) {
        val cached = cachedExplorePlaces?.firstOrNull { isCached(it, latLng, offset) }
        // cache data if it was not cached before
        if (cached == null) {
            cachedExplorePlaces?.add(PlaceCache(latLng, offset, places))
        }
    }

    /**
     * Predicate function to decide whether data is cached or not
     * @param placeCache cache venues list
     * @param latLng requested location
     * @param offset request offset
     */
    private fun isCached(placeCache: PlaceCache, latLng: String, offset: Int): Boolean =
        (locationUtil.distanceBetweenTwoPoints(placeCache.latLng, latLng) < MIN_DISTANCE) && placeCache.offset == offset
}