package android.azadevs.xchange.data.remote.service

import android.azadevs.xchange.data.remote.model.CurrencyResponse
import retrofit2.http.GET

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */
interface ExchangeServiceApi {

    @GET("exchange-rates/json/")
    suspend fun getExchangeRates(): List<CurrencyResponse>

}