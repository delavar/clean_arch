package com.delavar.arch.data.mapper

import com.delavar.arch.domain.response.DomainErrorException
import com.delavar.arch.domain.response.ErrorStatus
import com.delavar.arch.data.util.providers.ResourceProvider
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException

class CloudErrorMapperTest {
    lateinit var errorMapper: CloudErrorMapper
    val gson: Gson =Gson()

    @Before
    fun setUp() {
        errorMapper= CloudErrorMapper(gson,mock<ResourceProvider>())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun mapToErrorExceptionTimeout() {
        val result=errorMapper.mapToErrorException(SocketTimeoutException())
        assertTrue(result is DomainErrorException)
        result as DomainErrorException
        assertEquals(result.errorModel.errorStatus,ErrorStatus.TIMEOUT)
    }

    @Test
    fun mapToErrorExceptionNoConnection() {
        val result=errorMapper.mapToErrorException(IOException())
        assertTrue(result is DomainErrorException)
        result as DomainErrorException
        assertEquals(result.errorModel.errorStatus,ErrorStatus.NO_CONNECTION)
    }

    @Test
    fun mapToErrorExceptionUnAuthorized() {
        val result=errorMapper.mapToErrorException(HttpException(Response.error<String>(401, mock<ResponseBody>())))
        assertTrue(result is DomainErrorException)
        result as DomainErrorException
        assertEquals(result.errorModel.errorStatus,ErrorStatus.UNAUTHORIZED)
    }

    @Test
    fun mapToErrorExceptionNotDefined() {
        val response = ResponseBody.create(null , "internal error")
        val result= errorMapper.mapToErrorException(HttpException(Response.error<String>(500, response)))
        assertTrue(result is DomainErrorException)
        result as DomainErrorException
        assertEquals(result.errorModel.errorStatus,ErrorStatus.NOT_DEFINED)
    }

    @Test
    fun mapToErrorExceptionBadResponse() {
        val response = ResponseBody.create(null , "{response:\"error\",meta:{code:111,requestId:\"123\"}}")
        val result= errorMapper.mapToErrorException(HttpException(Response.error<String>(500, response)))
        assertTrue(result is DomainErrorException)
        result as DomainErrorException
        assertEquals(result.errorModel.errorStatus,ErrorStatus.BAD_RESPONSE)
    }

    @Test
    fun mapToErrorException() {
        val result=errorMapper.mapToErrorException(Exception())
        assertFalse(result is DomainErrorException)
    }
}