package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.model.Currency
import kotlinx.coroutines.flow.Flow

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
interface GetCurrenciesUseCase {

    operator fun invoke(): Flow<Resource<List<Currency>, Error>>

}