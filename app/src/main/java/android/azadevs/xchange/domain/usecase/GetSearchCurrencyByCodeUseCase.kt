package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.domain.model.Currency
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
interface GetSearchCurrencyByCodeUseCase {

    operator fun invoke(name: String): Flow<List<Currency>>

}