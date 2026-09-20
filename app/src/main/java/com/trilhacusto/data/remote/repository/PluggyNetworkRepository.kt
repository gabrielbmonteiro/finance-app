package com.trilhacusto.data.remote.repository

import com.trilhacusto.data.remote.api.PluggyApiService
import com.trilhacusto.data.remote.dto.PluggyAuthRequest
import com.trilhacusto.data.remote.dto.PluggyTransactionDto
import com.trilhacusto.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class PluggyNetworkRepository(
    private val apiService: PluggyApiService,
    private val userPrefs: UserPreferencesRepository
) {
    suspend fun fetchTransactions(accountId: String): Result<List<PluggyTransactionDto>> {
        return withContext(Dispatchers.IO) {
            try {
                val clientId = userPrefs.pluggyClientId.firstOrNull() ?: ""
                val clientSecret = userPrefs.pluggyClientSecret.firstOrNull() ?: ""
                
                val authReq = PluggyAuthRequest(clientId, clientSecret)
                val authRes = apiService.auth(authReq)
                
                if (!authRes.isSuccessful || authRes.body() == null) {
                    return@withContext Result.failure(Exception("Falha na autenticação da Pluggy: ${authRes.code()}"))
                }
                
                val apiKey = authRes.body()!!.apiKey


                val response = apiService.getTransactions(apiKey = apiKey, accountId = accountId)
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body.results)
                    } else {
                        Result.failure(Exception("A API retornou sucesso, mas o corpo está vazio."))
                    }
                } else {
                    Result.failure(Exception("Erro na API da Pluggy: Código ${response.code()} - ${response.message()}"))
                }
            } catch (e: HttpException) {
                Result.failure(Exception("HttpException: ${e.message()}"))
            } catch (e: IOException) {
                Result.failure(Exception("Falha de conexão com a Internet. Verifique sua rede."))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
