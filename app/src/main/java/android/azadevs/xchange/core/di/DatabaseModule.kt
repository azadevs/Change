package android.azadevs.xchange.core.di

import android.azadevs.xchange.data.local.XchangeDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): XchangeDatabase {
        return Room.databaseBuilder(
            app,
            XchangeDatabase::class.java,
            "xchange_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(db: XchangeDatabase) = db.currencyDao()

    @Provides
    @Singleton
    fun provideCurrencyDateDao(db: XchangeDatabase) = db.dateDao()

}