package com.sipues.data.network

import com.sipues.data.model.response.Business
import retrofit2.http.GET

interface BusinessApiService {
    @GET("business")
    suspend fun getBusiness(): List<Business>
}

