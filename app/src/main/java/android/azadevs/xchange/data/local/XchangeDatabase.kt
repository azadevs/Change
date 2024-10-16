package android.azadevs.xchange.data.local

import android.azadevs.xchange.data.local.dao.CurrencyDao
import android.azadevs.xchange.data.local.dao.CurrencyDateDao
import android.azadevs.xchange.data.local.entity.CurrencyDateEntity
import android.azadevs.xchange.data.local.entity.CurrencyEntity
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@Database(
    entities = [CurrencyEntity::class, CurrencyDateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class XchangeDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun dateDao(): CurrencyDateDao

}