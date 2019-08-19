package com.delavar.arch.data.mapper

import com.delavar.arch.data.R
import com.delavar.arch.data.model.ResponseModel
import com.delavar.arch.domain.response.DomainErrorException
import com.delavar.arch.domain.response.ErrorModel
import com.delavar.arch.domain.response.ErrorStatus
import com.delavar.arch.data.util.providers.ResourceProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class CloudErrorMapper @Inject constructor(private val gson: Gson,
                                           private val resourceProvider: ResourceProvider
) {

    fun mapToErrorException(throwable: Throwable?): Throwable? {
        val errorModel: ErrorModel? = when (throwable) {

            // if throwable is an instance of HttpException
            // then attempt to parse error data from response body
            is HttpException -> {
                // handle UNAUTHORIZED situation (when token expired)
                if (throwable.code() == 401) {
                    ErrorModel(ErrorStatus.UNAUTHORIZED,resourceProvider.getString(R.string.error_unauthorized))
                } else {
                    getHttpError(throwable.response()?.errorBody())
                }
            }

            // handle api call timeout error
            is SocketTimeoutException -> {
                ErrorModel(ErrorStatus.TIMEOUT,resourceProvider.getString(R.string.error_timeout))
            }

            // handle connection error
            is IOException -> {
                ErrorModel(ErrorStatus.NO_CONNECTION,resourceProvider.getString(R.string.error_no_connection))
            }
            else -> null
        }
        return errorModel?.let { DomainErrorException(it) } ?: throwable
    }

    /**
     * attempts to parse http response body and get error data from it
     *
     * @param body retrofit response body
     * @return returns an instance of [ErrorModel] with parsed data or NOT_DEFINED status
     */
    private fun getHttpError(body: ResponseBody?): ErrorModel {
        return try {
            val listType = object : TypeToken<ResponseModel<Any>>() {}.type
            val remoteResponse: ResponseModel<Any> = gson.fromJson(body?.string(), listType)
            ErrorModel(remoteResponse.meta.errorDetail, remoteResponse.meta.code, ErrorStatus.BAD_RESPONSE)
        } catch (e: Throwable) {
            e.printStackTrace()
            ErrorModel(ErrorStatus.NOT_DEFINED)
        }

    }
}