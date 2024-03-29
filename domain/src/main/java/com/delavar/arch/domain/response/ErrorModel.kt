package com.delavar.arch.domain.response

data class ErrorModel(
    val message: String?,
    val code: Int?,
    @Transient var errorStatus: ErrorStatus
) {
    constructor(errorStatus: ErrorStatus) : this(null, null, errorStatus)
    constructor(errorStatus: ErrorStatus,message: String?) : this(message, null, errorStatus)
}