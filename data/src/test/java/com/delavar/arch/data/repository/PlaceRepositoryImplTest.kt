package com.delavar.arch.data.repository

import com.delavar.arch.data.source.cache.CachePlaceSource
import com.delavar.arch.data.source.cloud.CloudPlaceSource
import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.model.PlaceGroupItemModel
import com.delavar.arch.domain.model.PlaceGroupModel
import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.data.model.MetaModel
import com.delavar.arch.data.model.ResponseModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Flowable
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PlaceRepositoryImplTest {
    @Mock
    lateinit var cloudSource: CloudPlaceSource
    @Mock
    lateinit var cacheSource: CachePlaceSource

    lateinit var placeRepositoryImpl: PlaceRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        placeRepositoryImpl = PlaceRepositoryImpl(cloudSource, cacheSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getPlaceListFromCache() {
        val places = arrayListOf(Place("1", "delavar"))
        Mockito.`when`(cacheSource.getPlaceList(any(), any())).thenReturn(places)
        val result = placeRepositoryImpl.getPlaceList("22.0001,33.1001", any(), any())
        verify(cacheSource).getPlaceList(any(), any())
        result.test().assertResult(places)
    }


    @Test
    fun getPlaceListFromCloud() {
        val firstPlace = Place("1", "delavar")

        Mockito.`when`(cloudSource.getPlaceList(any(), any(), any()))
            .thenReturn(Flowable.just(makeMockResponse(firstPlace)))

        Mockito.`when`(cacheSource.getPlaceList(any(), any())).thenReturn(emptyList())
        val result = placeRepositoryImpl.getPlaceList("22.0001,33.1001", any(), any())
        verify(cloudSource).getPlaceList(any(), any(), any())
        result.test().assertValue {it.first().equals(firstPlace)}
    }

    fun makeMockResponse(firstPlace: Place): ResponseModel<PlaceListData> {
        val apiResponse = ResponseModel(
            PlaceListData(
                1,
                arrayListOf(
                    PlaceGroupModel(
                        "t",
                        "name",
                        arrayListOf(PlaceGroupItemModel(firstPlace))
                    )
                )
            ),
            MetaModel(200, "11")
        )
        return apiResponse
    }
}