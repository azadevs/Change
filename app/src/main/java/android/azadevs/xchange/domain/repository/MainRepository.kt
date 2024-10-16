package android.azadevs.xchange.domain.repository

import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.model.CurrencyWithDate
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */
interface MainRepository {

    suspend fun getCurrencies(): List<Currency>

    suspend fun getCurrencyByCode(name: String): Currency

    fun getSearchCurrencyByCode(code: String): Flow<List<Currency>>

    fun getCurrenciesFromDatabase(): Flow<List<Currency>>

    fun getCurrencyWithDates(code: String): Flow<List<CurrencyWithDate>>

}