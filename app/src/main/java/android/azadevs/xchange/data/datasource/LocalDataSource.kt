package android.azadevs.xchange.data.datasource

import android.azadevs.xchange.core.mappers.toCurrency
import android.azadevs.xchange.core.mappers.toCurrencyDateEntityList
import android.azadevs.xchange.core.mappers.toCurrencyList
import android.azadevs.xchange.data.local.dao.CurrencyDao
import android.azadevs.xchange.data.local.dao.CurrencyDateDao
import android.azadevs.xchange.data.local.entity.CurrencyEntity
import android.azadevs.xchange.domain.model.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
@Singleton
class LocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao, private val currencyDateDao: CurrencyDateDao
) {

    fun getCurrencyWithDates(code: String) = currencyDateDao.getCurrencyDate(code)

    suspend fun upsertCurrencyDate(
        currency: List<CurrencyEntity>
    ) {
        withContext(Dispatchers.IO) {
            val currencyWithDates = currencyDateDao.getCurrencyDate()
            if (currencyWithDates.isEmpty()) {
                currencyDateDao.upsertCurrencyDate(currency.toCurrencyDateEntityList())
            } else {
                if (currencyWithDates.last().date != currency.last().date) {
                    currencyDateDao.upsertCurrencyDate(currency.toCurrencyDateEntityList())
                }
            }
        }
    }

    suspend fun getCurrencyByCode(name: String): Currency {
        return currencyDao.getCurrencyByCode(name).toCurrency()
    }

    fun getSearchCurrencyByCode(name: String): Flow<List<Currency>> {
        return currencyDao.getSearchCurrencyByCode(name).map {
            if (it.isEmpty()) {
                emptyList()
            } else {
                it.toCurrencyList()
            }
        }
    }

    suspend fun getCurrencies(): List<CurrencyEntity> {
        return currencyDao.getCurrencies()
    }

    suspend fun upsertCurrencies(response: List<CurrencyEntity>) {
        currencyDao.upsertCurrencies(response)
    }
}