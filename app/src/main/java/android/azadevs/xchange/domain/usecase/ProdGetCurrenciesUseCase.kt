package android.azadevs.xchange.domain.usecase

import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */

class ProdGetCurrenciesUseCase @Inject constructor(
    private val mainRepository: MainRepository
) : GetCurrenciesUseCase {

    override fun invoke(): Flow<Resource<List<Currency>, Error>> = flow {
        try {
            val result = mainRepository.getCurrencies()
            if (result.isEmpty()) {
                emit(Resource.Error(Error.NoInternet))
            } else {
                emit(Resource.Success(mainRepository.getCurrencies()))
            }
        } catch (e: IOException) {
            emit(Resource.Error(Error.NoInternet))
        } catch (e: HttpException) {
            emit(Resource.Error(Error.ServerError(e.message ?: "Unknown server error")))
        } catch (e: Exception) {
            emit(Resource.Error(Error.Unknown(e.message ?: "Unknown error")))
        }
    }
}