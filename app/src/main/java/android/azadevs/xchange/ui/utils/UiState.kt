package android.azadevs.xchange.ui.utils

/**
 * Created by : Azamat Kalmurzaev
 * 08/10/24
 */
sealed class UiState<out T : Any> {

    data object Loading : UiState<Nothing>()

    data class Success<out T : Any>(val data: T) : UiState<T>()

    data class Error(val message: String) : UiState<Nothing>()

    data object Empty : UiState<Nothing>()

}