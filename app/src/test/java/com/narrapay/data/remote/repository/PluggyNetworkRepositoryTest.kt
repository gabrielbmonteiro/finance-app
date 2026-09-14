package com.narrapay.data.remote.repository

import com.narrapay.data.remote.api.PluggyApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PluggyNetworkRepositoryTest {

    private val apiService: PluggyApiService = mockk()
    private val repository = PluggyNetworkRepository(apiService)

    @Test
    fun deve_retornar_failure_quando_api_retornar_http_500() = runTest {
        // Arrange
        val errorResponseBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
        val errorResponse = Response.error<Any>(500, errorResponseBody)
        val httpException = HttpException(errorResponse)
        
        coEvery { apiService.getTransactions(any()) } throws httpException

        // Act
        val result = repository.fetchTransactions("acc_123")

        // Assert
        assertTrue("Deveria retornar failure ao receber HTTP 500", result.isFailure)
        
        val exception = result.exceptionOrNull()
        assertTrue("A exceção deveria relatar HttpException", exception?.message?.contains("HttpException") == true)
    }

    @Test
    fun deve_retornar_failure_quando_nao_houver_internet() = runTest {
        // Arrange
        coEvery { apiService.getTransactions(any()) } throws IOException("Sem Internet")

        // Act
        val result = repository.fetchTransactions("acc_123")

        // Assert
        assertTrue("Deveria retornar failure ao ter IOException", result.isFailure)
        
        val exception = result.exceptionOrNull()
        assertTrue("A exceção deveria avisar sobre a rede", exception?.message?.contains("Falha de conexão com a Internet") == true)
    }
}
