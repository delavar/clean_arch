package com.delavar.arch.domain.usecase

import com.delavar.arch.domain.executor.DomainScheduler
import com.delavar.arch.domain.model.Place
import com.delavar.arch.domain.repository.FakePlaceRepository
import com.delavar.arch.domain.response.SuccessResponse
import com.delavar.arch.domain.response.UseCaseResponse
import com.nhaarman.mockitokotlin2.argumentCaptor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PlaceListUseCaseTest {

    @Mock
    lateinit var domainScheduler: DomainScheduler

    @Mock
    lateinit var onResponse: (UseCaseResponse<List<Place>>?) -> Unit
    @Mock
    lateinit var onTokenExpired: () -> Unit

    lateinit var testScheduler: TestScheduler
    lateinit var placeUseCase: PlaceListUseCase

    @Before
    fun setUp() {
        testScheduler = TestScheduler()

        MockitoAnnotations.initMocks(this)

        placeUseCase = PlaceListUseCase(
            FakePlaceRepository(),
            domainScheduler
        )

        Mockito.`when`(domainScheduler.io()).thenReturn(testScheduler)
        Mockito.`when`(domainScheduler.ui()).thenReturn(testScheduler)

        RxJavaPlugins.reset()
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
    }

    @Test
    fun testGettingPlaceList() {
        placeUseCase.setParameters(22.22, 33.33, 10, 0)
        placeUseCase.execute(CompositeDisposable(), onResponse, onTokenExpired)
        testScheduler.triggerActions()
        val captor = argumentCaptor<UseCaseResponse<List<Place>>>()
        verify(onResponse, times(1)).invoke(captor.capture())
        val places = (captor.firstValue as SuccessResponse).value

        val place1 = Place("1", "yara company")
        val place2 = Place("2", "azadi")
        assertEquals(places, arrayListOf(place1, place2))
    }
}