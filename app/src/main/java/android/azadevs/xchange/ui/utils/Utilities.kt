package android.azadevs.xchange.ui.utils

import android.azadevs.xchange.ui.model.CurrencyDisplayItem

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */

fun getCurrencyCodes(list: List<CurrencyDisplayItem>) = list.map { it.code }
