package android.azadevs.xchange.ui.currency.viewmodel

import android.azadevs.xchange.core.mappers.toCurrencyDisplayItem
import android.azadevs.xchange.core.mappers.toCurrencyDisplayItemList
import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.usecase.GetCurrenciesUseCase
import android.azadevs.xchange.domain.usecase.GetSearchCurrencyByCodeUseCase
import android.azadevs.xchange.ui.utils.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    getCurrenciesUseCase: GetCurrenciesUseCase,
    private val searchCurrencyByCodeUseCase: GetSearchCurrencyByCodeUseCase
) : ViewModel() {

    private val searchQueryFlow = MutableSharedFlow<String>()

    var currenciesFlow = getCurrenciesUseCase().map { result ->
        when (result) {
            is Resource.Error -> {
                when (result.error) {
                    Error.NoInternet -> {
                        UiState.Error("No Internet Connection. Please check your internet connection and try again.")
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
    }.catch {
        emit(UiState.Error(it.message ?: "Unknown error"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchCurrenciesFlow = searchQueryFlow.flatMapLatest {
        searchCurrencyByCodeUseCase(it).map { currencies -> currencies.toCurrencyDisplayItemList() }
    }.catch {
        emit(emptyList())
    }

    fun searchCurrencyByCode(code: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(code)
        }
    }
}