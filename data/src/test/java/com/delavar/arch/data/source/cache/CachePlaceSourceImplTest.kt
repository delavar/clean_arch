package com.delavar.arch.data.source.cache

import com.delavar.arch.data.util.LocationUtil
import com.delavar.arch.domain.model.Place
import com.nhaarman.mockitokotlin2.any
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CachePlaceSourceImplTest {
    @Mock
    lateinit var locationUtil: LocationUtil
    lateinit var cacheSourceImpl: CachePlaceSourceImpl
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getCachePlaceList() {
        cacheSourceImpl = CachePlaceSourceImpl(locationUtil)

        val places = arrayListOf(Place("1", "yara company"))
        cacheSourceImpl.storePlaceList("11.0001,22.1233",
            0,
            places
        )
        Mockito.`when`(locationUtil.distanceBetweenTwoPoints(any(),any())).thenReturn(90F)
        val result =cacheSourceImpl.getPlaceList("11.00010,22.1233",0)
        assertTrue(result.isNotEmpty())
        assertEquals(places,result)
    }

    @Test
    fun getCachePlaceListWithLongDistance() {
        cacheSourceImpl = CachePlaceSourceImpl(locationUtil)

        val places = arrayListOf(Place("1", "yara company"))
        cacheSourceImpl.storePlaceList("11.0001,22.1233",
            0,
            places
        )
        Mockito.`when`(locationUtil.distanceBetweenTwoPoints(any(),any())).thenReturn(100F)
        val result =cacheSourceImpl.getPlaceList("11.00010,22.1233",0)
        assertTrue(result.isEmpty())
        assertNotEquals(places,result)
    }

    @Test
    fun whenCachePlaceListIsExpired() {
        cacheSourceImpl = CachePlaceSourceImpl(locationUtil,4)

        val places = arrayListOf(Place("1", "yara company"))
        cacheSourceImpl.storePlaceList("11.0001,22.1233",
            0,
            places
        )
        Mockito.`when`(locationUtil.distanceBetweenTwoPoints(any(),any())).thenReturn(90F)
        val result =cacheSourceImpl.getPlaceList("11.00010,22.1233",0)
        assertTrue(result.isEmpty())
        assertNotEquals(places,result)
    }

}