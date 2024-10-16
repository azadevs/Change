package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.model.Currency
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 07/10/24
 */
interface GetCurrencyByCodeUseCase {

    operator fun invoke(code: String): Flow<Resource<Currency, Error>>

}