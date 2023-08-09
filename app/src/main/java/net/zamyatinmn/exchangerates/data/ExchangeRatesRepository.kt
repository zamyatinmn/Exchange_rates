package net.zamyatinmn.exchangerates.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.zamyatinmn.exchangerates.data.local.Currency
import net.zamyatinmn.exchangerates.data.local.FavoritesDao
import net.zamyatinmn.exchangerates.data.remote.ExchangeRatesRemoteDataSource
import net.zamyatinmn.exchangerates.data.remote.Rate
import javax.inject.Inject


class ExchangeRatesRepository @Inject constructor(
    private val remoteDataSource: ExchangeRatesRemoteDataSource,
    private val favoritesDao: FavoritesDao,
) {
    suspend fun getLatest(base: Rate?) =
        withContext(Dispatchers.IO) { remoteDataSource.fetchLatest(base) }

    suspend fun getFavorites() = withContext(Dispatchers.IO) { favoritesDao.getAll() }

    suspend fun addToFavorites(rate: Rate) {
        withContext(Dispatchers.IO) { favoritesDao.insert(Currency(id = rate.id)) }
    }

    suspend fun removeFromFavorites(rate: Rate) {
        withContext(Dispatchers.IO) { favoritesDao.delete(Currency(id = rate.id)) }
    }
}