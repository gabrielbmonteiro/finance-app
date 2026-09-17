package com.trilhacusto.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PluggyTransactionsResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<PluggyTransactionDto>
)

data class PluggyTransactionDto(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("date") val date: String,
    @SerializedName("category") val category: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("creditCardMetadata") val creditCardMetadata: CreditCardMetadataDto?,
    @SerializedName("merchant") val merchant: MerchantDto?
)

data class CreditCardMetadataDto(
    @SerializedName("installmentNumber") val installmentNumber: Int?,
    @SerializedName("totalInstallments") val totalInstallments: Int?,
    @SerializedName("cardNumber") val cardNumber: String?,
    @SerializedName("billForecastDate") val billForecastDate: String?
)

data class MerchantDto(
    @SerializedName("name") val name: String?,
    @SerializedName("businessName") val businessName: String?,
    @SerializedName("cnpj") val cnpj: String?,
    @SerializedName("cnae") val cnae: String?
)

data class PluggyAuthRequest(
    @SerializedName("clientId") val clientId: String,
    @SerializedName("clientSecret") val clientSecret: String
)

data class PluggyAuthResponse(
    @SerializedName("apiKey") val apiKey: String
)
