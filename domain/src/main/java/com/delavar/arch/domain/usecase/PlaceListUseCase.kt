package com.delavar.arch.domain.usecase

import com.delavar.arch.domain.executor.DomainScheduler
import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.repository.PlaceRepository
import io.reactivex.Flowable
import javax.inject.Inject

class PlaceListUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
    scheduler: DomainScheduler
) : UseCase<List<Place>>(scheduler) {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var limit: Int = 0
    private var offset: Int = 0

    fun setParameters(latitude: Double, longitude: Double, limit: Int, offset: Int) = this.apply {
        this.latitude = latitude
        this.longitude = longitude
        this.limit = limit
        this.offset = offset
    }

    override fun buildUseCaseObservable(): Flowable<List<Place>> =
        placeRepository.getPlaceList("$latitude,$longitude", limit, offset)

}