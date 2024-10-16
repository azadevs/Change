package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.domain.model.CurrencyWithDate
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
interface GetCurrencyWithDatesUseCase {

    operator fun invoke(code: String): Flow<List<CurrencyWithDate>>

}