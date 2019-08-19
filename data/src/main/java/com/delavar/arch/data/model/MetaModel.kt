package com.delavar.arch.data.model

import com.google.gson.annotations.SerializedName

data class MetaModel(
    @SerializedName("code") val code: Int,
    @SerializedName("requestId") val requestId: String,
    @SerializedName("errorType") val errorType: String? = null,
    @SerializedName("errorDetail") val errorDetail: String? = null
)