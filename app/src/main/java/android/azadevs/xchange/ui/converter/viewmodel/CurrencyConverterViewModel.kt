package android.azadevs.xchange.ui.converter.viewmodel

import android.azadevs.xchange.R
import android.azadevs.xchange.core.mappers.toCurrencyDisplayItem
import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.usecase.GetCurrenciesUseCase
import android.azadevs.xchange.ui.utils.UIText
import android.azadevs.xchange.ui.utils.UiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 13/10/24
 */
@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    currenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    val currenciesFlow = currenciesUseCase()
        .map { state ->
            when (state) {
                is Resource.Error -> {
                    when (state.error) {
                        Error.NoInternet -> {
                            UiState.Error(UIText.ResourceText(R.string.text_no_internet))
                        }

                        is Error.ServerError -> {
                            UiState.Error(UIText.DynamicText(state.error.errorMessage))
                        }

                        is Error.Unknown -> {
                            UiState.Error(UIText.DynamicText(state.error.errorMessage))
                        }

                    }
                }

                is Resource.Success -> {
                    UiState.Success(state.data.map { it.toCurrencyDisplayItem() })
                }
            }
        }
        .catch {
            emit(
                UiState.Error(
                    if (it.message != null) {
                        UIText.DynamicText(it.message!!)
                    } else {
                        UIText.ResourceText(R.string.text_unknown_error)
                    }
                )
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState.Loading)
}