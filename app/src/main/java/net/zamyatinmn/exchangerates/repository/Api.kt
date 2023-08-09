package net.zamyatinmn.exchangerates.repository

import net.zamyatinmn.exchangerates.data.remote.ExchangeRatesResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ExchangeRatesApi {

    @GET("/latest")
    suspend fun getLatest(@Query("base") base: String?): ExchangeRatesResponse

}