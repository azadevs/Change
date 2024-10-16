package android.azadevs.xchange.core.utils

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */


sealed interface Error {

    data object NoInternet : Error

    data class Unknown(val errorMessage: String) : Error

    data class ServerError(val errorMessage: String) : Error
}
