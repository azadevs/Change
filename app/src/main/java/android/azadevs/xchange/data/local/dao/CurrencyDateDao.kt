package android.azadevs.xchange.data.local.dao

import android.azadevs.xchange.data.local.entity.CurrencyDateEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
@Dao
interface CurrencyDateDao {

    @Upsert
    suspend fun upsertCurrencyDate(currencies: List<CurrencyDateEntity>)

    @Query("SELECT * FROM currency_date WHERE code=:code")
    fun getCurrencyDate(code: String): Flow<List<CurrencyDateEntity>>

    @Query("SELECT * FROM currency_date")
    suspend fun getCurrencyDate(): List<CurrencyDateEntity>


}