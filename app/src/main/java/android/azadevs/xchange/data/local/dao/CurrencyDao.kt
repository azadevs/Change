package android.azadevs.xchange.data.local.dao

import android.azadevs.xchange.data.local.entity.CurrencyEntity
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency")
    suspend fun getCurrencies(): List<CurrencyEntity>

    @Upsert
    suspend fun upsertCurrencies(currencies: List<CurrencyEntity>)

    @Query("SELECT * FROM currency WHERE code = :code")
    suspend fun getCurrencyByCode(code: String): CurrencyEntity

    @Query("SELECT * FROM currency WHERE code LIKE '%' || :code || '%'")
    fun getSearchCurrencyByCode(code: String): Flow<List<CurrencyEntity>>
}