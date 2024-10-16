package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 07/10/24
 */
class ProdGetCurrencyByCodeUseCase @Inject constructor(
    private val mainRepository: MainRepository
) : GetCurrencyByCodeUseCase {
    override fun invoke(code: String): Flow<Resource<Currency, Error>> = flow {
        try {
            emit(Resource.Success(mainRepository.getCurrencyByCode(code)))
        } catch (e: IOException) {
            emit(Resource.Error(Error.NoInternet))
        } catch (e: Exception) {
            emit(Resource.Error(Error.Unknown(e.message ?: "Unknown error")))
        }
    }
}