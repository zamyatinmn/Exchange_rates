package net.zamyatinmn.exchangerates.data.remote

import kotlinx.serialization.Serializable


@Serializable
data class ExchangeRatesResponse(
    val success: Boolean = false,
    val error: ApiError? = null,
    val timestamp: Long? = null,
    val base: String? = null,
    val date: String? = null,
    val rates: Map<String, Double>? = null,
)

@Serializable
data class ApiError(
    val code: Int? = null,
    val type: String? = null,
    val info: String? = null,
)