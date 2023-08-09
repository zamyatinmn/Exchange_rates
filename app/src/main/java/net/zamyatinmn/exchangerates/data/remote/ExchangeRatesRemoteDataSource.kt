package net.zamyatinmn.exchangerates.data.remote

import net.zamyatinmn.exchangerates.repository.ExchangeRatesApi
import retrofit2.Retrofit
import javax.inject.Inject


class ExchangeRatesRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchLatest(base: Rate?): Result {
        val result = retrofit.create(ExchangeRatesApi::class.java).getLatest(base?.id)
        return try {
            if (result.success) {
                val data = result.rates?.map {
                    Rate(it.key, it.value)
                }
                Result.Success(data!!)
            } else {
                Result.Error(result.error?.type!!)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }
}

data class Rate(
    val id: String,
    val rate: Double = 0.0,
    val isFavorite: Boolean = false,
)

sealed class Result {
    data class Success(val data: List<Rate>): Result()
    data class Error(val reason: String): Result()
}