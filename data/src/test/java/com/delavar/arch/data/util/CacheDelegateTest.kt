package com.delavar.arch.data.util

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit

class CacheDelegateTest {
    @Mock
    lateinit var predicateExpired :(String) -> Boolean

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testCache(){
        var test:String? by CacheDelegate(CacheDelegate.CACHE_FOR_10_MINUTES, predicateExpired)
        test = "text"
        assertEquals(test,"text")
    }

    @Test
    fun testPredicateExpired(){
        var test:String? by CacheDelegate(2, predicateExpired)
        test = "text"
        TimeUnit.MILLISECONDS.sleep(2L)
        assertNull(test)
        val captor= argumentCaptor<String>()
        verify(predicateExpired,times(1)).invoke(captor.capture())
        assertEquals("text",captor.firstValue)
    }
}