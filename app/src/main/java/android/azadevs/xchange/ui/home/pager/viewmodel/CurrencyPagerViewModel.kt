package android.azadevs.xchange.ui.home.pager.viewmodel

import android.azadevs.xchange.R
import android.azadevs.xchange.core.mappers.toCurrencyDisplayItem
import android.azadevs.xchange.core.utils.Error
import android.azadevs.xchange.core.utils.Resource
import android.azadevs.xchange.domain.usecase.GetCurrencyByCodeUseCase
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.azadevs.xchange.ui.utils.Constants.ARG_CURRENCY_CODE
import android.azadevs.xchange.ui.utils.UIText
import android.azadevs.xchange.ui.utils.UiState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by : Azamat Kalmurzaev
 * 08/10/24
 */
@HiltViewModel
class CurrencyPagerViewModel @Inject constructor(
    getCurrencyByCode: GetCurrencyByCodeUseCase,
    stateHandle: SavedStateHandle
) : ViewModel() {

    val currentCurrencyFlow: StateFlow<UiState<CurrencyDisplayItem>> =
        getCurrencyByCode(stateHandle[ARG_CURRENCY_CODE]!!)
            .map { result ->
                when (result) {
                    is Resource.Error -> {
                        when (result.error) {
                            Error.NoInternet -> {
                                UiState.Error(UIText.ResourceText(R.string.text_no_internet))
                            }

                            is Error.ServerError -> {
                                UiState.Error(UIText.DynamicText(result.error.errorMessage))
                            }

                            is Error.Unknown -> {
                                UiState.Error(UIText.DynamicText(result.error.errorMessage))
                            }
                        }
                    }

                    is Resource.Success -> UiState.Success(
                        result.data.toCurrencyDisplayItem()
                    )
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
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )
}
