package com.delavar.arch.data.source.cloud

import com.delavar.arch.data.mapper.CloudErrorMapper
import com.delavar.arch.data.restful.ApiService
import com.delavar.arch.data.util.FoursquareKey
import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.data.model.ResponseModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class CloudPlaceSourceImplTest {
    lateinit var placeSource: CloudPlaceSourceImpl
    @Mock
    lateinit var apiService: ApiService
    @Mock
    lateinit var errorMapper: CloudErrorMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        placeSource = CloudPlaceSourceImpl(apiService, FoursquareKey, errorMapper)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getPlaceList() {
        configApiServiceForPlaceList()
        placeSource.getPlaceList("", 0, 10)
        verify(apiService).getPlaceList(
            any(),
            any(),
            any(),
            eq(FoursquareKey.getClientID()),
            eq(FoursquareKey.getClientSecret()),
            eq("20190525")
        )
    }

    private fun configApiServiceForPlaceList() {
        val flowable= Flowable.just(mock<ResponseModel<PlaceListData>>())
        Mockito.`when`(
            apiService.getPlaceList(
                any(),
                any(),
                any(),
                eq(FoursquareKey.getClientID()),
                eq(FoursquareKey.getClientSecret()),
                any()
            )
        ).thenReturn(flowable)
    }
}