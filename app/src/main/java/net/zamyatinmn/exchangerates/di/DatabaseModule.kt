package net.zamyatinmn.exchangerates.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zamyatinmn.exchangerates.data.local.AppDatabase
import net.zamyatinmn.exchangerates.data.local.FavoritesDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Provides
    fun provideFavoritesDao(appDatabase: AppDatabase): FavoritesDao {
        return appDatabase.favoritesDao()
    }
}