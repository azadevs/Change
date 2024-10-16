package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
class ProdSearchCurrencyByCodeUseCase @Inject constructor(
    private val mainRepository: MainRepository
) : GetSearchCurrencyByCodeUseCase {

    override fun invoke(name: String): Flow<List<Currency>> {
        return mainRepository.getSearchCurrencyByCode(name)
    }

}