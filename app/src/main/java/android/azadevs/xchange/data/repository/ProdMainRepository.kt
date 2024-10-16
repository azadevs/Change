package android.azadevs.xchange.data.repository

import android.azadevs.xchange.core.mappers.toCurrencyDateList
import android.azadevs.xchange.core.mappers.toCurrencyEntityList
import android.azadevs.xchange.core.mappers.toCurrencyList
import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.core.utils.XchangeUtilities.isNetworkAvailable
import android.azadevs.xchange.data.datasource.LocalDataSource
import android.azadevs.xchange.data.remote.service.ExchangeServiceApi
import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.model.CurrencyWithDate
import android.azadevs.xchange.domain.repository.MainRepository
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */

class ProdMainRepository @Inject constructor(
    private val exchangeServiceApi: ExchangeServiceApi,
    @ApplicationContext private val context: Context,
    private val localSource: LocalDataSource
) : MainRepository {

    override suspend fun getCurrencyByCode(name: String): Currency =
        localSource.getCurrencyByCode(name)

    override suspend fun getCurrencies(): List<Currency> {
        return if (!context.isNetworkAvailable()) {
            if (localSource.getCurrencies().isEmpty()) {
                emptyList()
            } else {
                localSource.getCurrencies().toCurrencyList()
            }
        } else {
            cacheCurrencies()
        }
    }

//    override suspend fun getCurrencies(): Resource<List<Currency>, Error> =
//        withContext(Dispatchers.IO) {
//            try {
//                if (!context.isNetworkAvailable()) {
//                    if (localSource.getCurrencies().isEmpty()) {
//                        Resource.Error(Error.NoInternet)
//                    } else {
//                        Resource.Success(localSource.getCurrencies().toCurrencyList())
//                    }
//                } else {
//                    Resource.Success(cacheCurrencies())
//                }
//            } catch (e: Exception) {
//                when (e) {
//                    is HttpException -> Resource.Error(
//                        Error.ServerError(
//                            e.message ?: "Unknown server error"
//                        )
//                    )
//
//                    else -> Resource.Error(Error.Unknown(e.message ?: "Unknown error"))
//                }
//            }
//        }

    override fun getSearchCurrencyByCode(code: String): Flow<List<Currency>> =
        localSource.getSearchCurrencyByCode(code)
            .flowOn(Dispatchers.Default)

    override fun getCurrenciesFromDatabase(): Flow<List<Currency>> = flow {
        localSource.getCurrencies()
    }

    override fun getCurrencyWithDates(code: String): Flow<List<CurrencyWithDate>> =
        localSource.getCurrencyWithDates(code).map {
            it.toCurrencyDateList()
        }.flowOn(Dispatchers.IO)

    private suspend fun cacheCurrencies(): List<Currency> {
        val response = exchangeServiceApi.getExchangeRates().toCurrencyEntityList()
        coroutineScope {
            launch {
                localSource.upsertCurrencyDate(response)
            }
            launch {
                localSource.upsertCurrencies(response)
            }
        }
        return localSource.getCurrencies().toCurrencyList()
    }

}