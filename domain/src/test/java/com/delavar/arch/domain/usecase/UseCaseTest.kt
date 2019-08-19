package com.delavar.arch.domain.usecase

import com.delavar.arch.domain.executor.DomainScheduler
import com.delavar.arch.domain.response.ErrorResponse
import com.delavar.arch.domain.response.SuccessResponse
import com.delavar.arch.domain.response.UseCaseResponse
import com.delavar.arch.domain.response.DomainErrorException
import com.delavar.arch.domain.response.ErrorModel
import com.delavar.arch.domain.response.ErrorStatus
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.capture
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class UseCaseTest {
    lateinit var compositeDisposable: CompositeDisposable
    @Mock
    lateinit var scheduler: DomainScheduler

    @Mock
    lateinit var onResponse: (UseCaseResponse<String>) -> Unit
    @Mock
    lateinit var onTokenExpired: () -> Unit

    lateinit var testScheduler: TestScheduler

    @Before
    fun before() {
        testScheduler = TestScheduler()
        compositeDisposable = Mockito.spy(CompositeDisposable())
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(scheduler.io()).thenReturn(testScheduler)
        given(scheduler.ui()).willReturn(testScheduler)
        RxJavaPlugins.reset()
    }

    @After
    fun after() {
        RxJavaPlugins.reset()
    }

    @Test
    fun executeWithErrorResponse() {
        //val onResponse=Mockito.spy({response:UseCaseResponse<String>?->})

        val useCaseError = UseCaseTestError(scheduler)
        useCaseError.execute(compositeDisposable, onResponse, onTokenExpired)
        testScheduler.advanceTimeBy(2, TimeUnit.MILLISECONDS)

        Mockito.verify(compositeDisposable, Mockito.times(1)).add(Mockito.any())

        val captor = argumentCaptor<UseCaseResponse<String>>()

        Mockito.verify(onResponse, Mockito.times(1)).invoke(captor.capture())
        assertTrue(captor.firstValue is ErrorResponse)
        val response = captor.firstValue as ErrorResponse
        assertEquals(response.error.errorStatus, ErrorStatus.BAD_RESPONSE)
        assertEquals(response.error.message, "bad response")
    }

    @Test
    fun executeWithSuccessResponse() {
        val useCase = UseCaseTestSuccess(scheduler)
        useCase.execute(compositeDisposable, onResponse, onTokenExpired)
        testScheduler.advanceTimeBy(2, TimeUnit.MILLISECONDS)
        Mockito.verify(compositeDisposable, Mockito.times(1)).add(Mockito.any())

        val captor = argumentCaptor<UseCaseResponse<String>>()

        Mockito.verify(onResponse, Mockito.times(1)).invoke(captor.capture())
        assertTrue(captor.firstValue is SuccessResponse)
        val response = captor.firstValue as SuccessResponse
        assertEquals(response.value, "response")
    }

    @Test
    fun executeTokenExpired() {
        val useCaseTokenExpired = UseCaseTestTokenExpired(scheduler)
        useCaseTokenExpired.execute(compositeDisposable, onResponse, onTokenExpired)
        testScheduler.advanceTimeBy(2, TimeUnit.MILLISECONDS)
        Mockito.verify(compositeDisposable, Mockito.times(1)).add(Mockito.any())
        Mockito.verify(onTokenExpired, Mockito.times(1)).invoke()

        val captor = argumentCaptor<UseCaseResponse<String>>()

        Mockito.verify(onResponse, Mockito.times(1)).invoke(captor.capture())
        assertTrue(captor.firstValue is ErrorResponse)
        val response = captor.firstValue as ErrorResponse
        assertEquals(response.error.errorStatus, ErrorStatus.UNAUTHORIZED)
        assertEquals(response.error.message, "token expired")
    }

    class UseCaseTestSuccess(
        scheduler: DomainScheduler
    ) : UseCase<String>(
        scheduler
    ) {
        override fun buildUseCaseObservable(): Flowable<String> {
            return Flowable.just("response")
        }
    }

    class UseCaseTestError(
        scheduler: DomainScheduler
    ) : UseCase<String>(
        scheduler
    ) {
        override fun buildUseCaseObservable(): Flowable<String> {
            return Flowable.error(DomainErrorException(ErrorModel(ErrorStatus.BAD_RESPONSE, "bad response")))
        }
    }

    class UseCaseTestTokenExpired(
        scheduler: DomainScheduler
    ) : UseCase<String>(
        scheduler
    ) {
        override fun buildUseCaseObservable(): Flowable<String> {
            return Flowable.error(DomainErrorException(ErrorModel(ErrorStatus.UNAUTHORIZED, "token expired")))
        }
    }
}