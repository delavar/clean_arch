package com.delavar.arch.data.restful

import com.delavar.arch.domain.model.PlaceListData
import com.delavar.arch.data.model.ResponseModel
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/venues/explore")
    fun getPlaceList(
        @Query("ll") latLng: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") apiVersion: String
    ): Flowable<ResponseModel<PlaceListData>>
}