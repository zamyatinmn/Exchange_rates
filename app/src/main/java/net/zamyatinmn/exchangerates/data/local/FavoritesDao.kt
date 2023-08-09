package net.zamyatinmn.exchangerates.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {
    @Query("SELECT * FROM currency")
    fun getAll(): Flow<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(currency: Currency)

    @Delete
    fun delete(currency: Currency)

    @Delete
    fun deleteAll(currencies: List<Currency>)
}