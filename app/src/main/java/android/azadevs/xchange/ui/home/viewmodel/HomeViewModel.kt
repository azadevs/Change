package android.azadevs.xchange.ui.home.viewmodel

import android.azadevs.xchange.core.mappers.toCurrencyDisplayItem
import android.azadevs.xchange.core.mappers.toHistoryItem
import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.usecase.GetCurrenciesUseCase
import android.azadevs.xchange.domain.usecase.GetCurrencyWithDatesUseCase
import android.azadevs.xchange.ui.model.CurrencyHistoryItem
import android.azadevs.xchange.ui.utils.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getCurrencyWithDatesUseCase: GetCurrencyWithDatesUseCase
) : ViewModel() {

    private var _currencyWithDates =
        MutableStateFlow<UiState<List<CurrencyHistoryItem>>>(UiState.Empty)
    val currencyWithDates = _currencyWithDates.asStateFlow()

    var currenciesFlow = getCurrenciesUseCase()
        .map { result ->
            when (result) {
                is Resource.Error -> {
                    when (result.error) {
                        Error.NoInternet -> {
                            UiState.Error("No Internet Connection. Please check your internet connection.")
                        }

                        is Error.ServerError -> {
                            UiState.Error(result.error.errorMessage)
                        }

                        is Error.Unknown -> {
                            UiState.Error(result.error.errorMessage)
                        }
                    }
                }
                is Resource.Success -> {
                    UiState.Success(result.data.map { it.toCurrencyDisplayItem() })
                }
            }
        }
        .catch {
            emit(UiState.Error(it.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun getCurrencyWithDates(code: String?) {
        _currencyWithDates.value = UiState.Loading
        viewModelScope.launch {
            getCurrencyWithDatesUseCase(code ?: "")
                .catch {
                    _currencyWithDates.value = UiState.Error(it.message ?: "Unknown error")
                }
                .map {
                    if (it.isEmpty()) {
                        _currencyWithDates.value = UiState.Error("No data found")
                    } else {
                        _currencyWithDates.value = UiState.Success(
                            it.map { currency ->
                                currency.toHistoryItem()
                            }
                        )
                    }
                }
        }
    }
}
