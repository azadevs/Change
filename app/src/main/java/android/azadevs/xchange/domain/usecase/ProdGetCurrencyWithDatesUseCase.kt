package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.domain.model.CurrencyWithDate
import android.azadevs.xchange.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 11/10/24
 */
class ProdGetCurrencyWithDatesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) : GetCurrencyWithDatesUseCase {

    override fun invoke(code: String): Flow<List<CurrencyWithDate>> = flow {
        mainRepository.getCurrencyWithDates(code)
            .catch {
                emit(emptyList())
            }
            .collect {
                emit(it)
            }
    }
}