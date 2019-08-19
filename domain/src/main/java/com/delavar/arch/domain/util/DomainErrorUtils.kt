package com.delavar.arch.domain.util

import com.delavar.arch.domain.response.DomainErrorException
import com.delavar.arch.domain.response.ErrorModel
import com.delavar.arch.domain.response.ErrorStatus
import java.lang.NullPointerException

object DomainErrorUtils {

    /**
     * Generate an instance of [ErrorModel] with happened [Throwable]
     * @param t happened [Throwable]
     *
     * @return rentuns an instance of [ErrorModel]
     */
    fun getErrorModel(t: Throwable?): ErrorModel {
        if (t is DomainErrorException)
            return t.errorModel

        // if response was successful but no data received
        if (t is NullPointerException) {
            return ErrorModel(ErrorStatus.EMPTY_RESPONSE)
        }

        // something happened that we are not make our self ready for it
        return ErrorModel(ErrorStatus.NOT_DEFINED)
    }
}