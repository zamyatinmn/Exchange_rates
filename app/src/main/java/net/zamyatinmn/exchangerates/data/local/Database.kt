package net.zamyatinmn.exchangerates.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}