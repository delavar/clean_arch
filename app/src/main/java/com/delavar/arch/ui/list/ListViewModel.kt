package com.delavar.arch.ui.list

import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.response.ErrorResponse
import com.delavar.arch.domain.response.SuccessResponse
import com.delavar.arch.domain.response.UseCaseResponse
import com.delavar.arch.domain.usecase.PlaceListUseCase
import com.delavar.arch.ui.base.BaseViewModel
import com.delavar.arch.util.livedata.NonNullLiveData
import com.delavar.arch.util.livedata.SingleEventLiveData
import com.delavar.arch.domain.response.ErrorModel
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val placeListUseCase: PlaceListUseCase
) : BaseViewModel() {
    val isLoading = NonNullLiveData(false)
    val places = NonNullLiveData<MutableList<Place>>(mutableListOf())
    val error = SingleEventLiveData<ErrorModel>()
    var limit: Int = 20
    var offset: Int = 0
        private set

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var canLoadMore = true

    fun setLocation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        this.offset = 0
        places.value = mutableListOf()
        loadData(latitude, longitude, limit, offset)
    }

    private fun loadData(latitude: Double, longitude: Double, limit: Int, offset: Int) {
        isLoading.value = true
        placeListUseCase
            .setParameters(latitude, longitude, limit, offset)
            .execute(compositeDisposable, this::onResponse, this::onTokenExpired)
    }

    private fun onResponse(response: UseCaseResponse<List<Place>>) {
        isLoading.value = false

        when (response) {
            is SuccessResponse -> {
                places.value = places.value.apply { addAll(response.value) }
                error.value = null
                checkLoadMore(response.value.size)
            }
            is ErrorResponse -> error.value = response.error
        }
    }

    private fun checkLoadMore(responseSize: Int) {
        offset = places.value.size
        canLoadMore = responseSize == limit
    }

    fun loadMore() {
        if (isLoading.value || !canLoadMore)
            return
        loadData(latitude, longitude, limit, offset)
    }
}