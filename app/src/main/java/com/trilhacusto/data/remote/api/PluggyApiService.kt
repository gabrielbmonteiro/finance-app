package com.trilhacusto.data.remote.api

import com.trilhacusto.data.remote.dto.PluggyAuthRequest
import com.trilhacusto.data.remote.dto.PluggyAuthResponse
import com.trilhacusto.data.remote.dto.PluggyTransactionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PluggyApiService {
    
    @POST("/auth")
    suspend fun auth(@Body request: PluggyAuthRequest): Response<PluggyAuthResponse>
    
    @GET("/v2/transactions")
    suspend fun getTransactions(
        @Header("X-API-KEY") apiKey: String,
        @Query("accountId") accountId: String
    ): Response<PluggyTransactionsResponse>
}
