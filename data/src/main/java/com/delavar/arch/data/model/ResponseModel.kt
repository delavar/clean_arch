package com.delavar.arch.data.model

import com.google.gson.annotations.SerializedName

data class ResponseModel<T>(
    @SerializedName("response") val response: T,
    @SerializedName("meta") val meta: MetaModel
)