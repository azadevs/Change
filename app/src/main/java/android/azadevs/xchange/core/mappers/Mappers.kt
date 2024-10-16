package android.azadevs.xchange.core.mappers

import android.azadevs.xchange.core.utils.XchangeUtilities.formatterCurrencyPrice
import android.azadevs.xchange.core.utils.XchangeUtilities.getDateFormat
import android.azadevs.xchange.core.utils.XchangeUtilities.getImageUrl
import android.azadevs.xchange.core.utils.XchangeUtilities.getTimeFormat
import android.azadevs.xchange.data.local.entity.CurrencyDateEntity
import android.azadevs.xchange.data.local.entity.CurrencyEntity
import android.azadevs.xchange.data.remote.model.CurrencyResponse
import android.azadevs.xchange.domain.model.Currency
import android.azadevs.xchange.domain.model.CurrencyWithDate
import android.azadevs.xchange.ui.model.CurrencyDisplayItem
import android.azadevs.xchange.ui.model.CurrencyHistoryItem

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */

fun CurrencyEntity.toCurrency(): Currency {
    return Currency(
        code = code,
        title = title,
        cbPrice = price,
        nbuBuyPrice = buyPrice ?: "",
        nbuCellPrice = sellPrice ?: "",
        date = date
    )
}

fun CurrencyResponse.toCurrencyEntity(): CurrencyEntity {
    return CurrencyEntity(
        price = cbPrice,
        code = code,
        date = date,
        buyPrice = nbuBuyPrice,
        sellPrice = nbuCellPrice,
        title = title
    )
}


fun Currency.toCurrencyDisplayItem(): CurrencyDisplayItem {
    return CurrencyDisplayItem(
        code = code,
        title = title,
        cbPrice = cbPrice,
        nbuBuyPrice = formatterCurrencyPrice(nbuBuyPrice),
        nbuCellPrice = formatterCurrencyPrice(nbuCellPrice),
        date = getDateFormat(date),
        currencyImageUri = getImageUrl(code)
    )
}

fun CurrencyEntity.toCurrencyDateEntity(): CurrencyDateEntity {
    return CurrencyDateEntity(
        date = date,
        nbBuyPrice = buyPrice ?: "",
        nbSellPrice = sellPrice ?: "",
        code = code
    )
}

fun CurrencyDateEntity.toCurrencyDate() = CurrencyWithDate(
    code = code,
    date = date,
    buyPrice = nbBuyPrice,
    sellPrice = nbSellPrice
)

fun CurrencyWithDate.toHistoryItem() = CurrencyHistoryItem(
    date = getDateFormat(date),
    nbuBuyPrice = formatterCurrencyPrice(buyPrice),
    nbuCellPrice = formatterCurrencyPrice(sellPrice),
    time = getTimeFormat(date)
)

fun List<CurrencyDateEntity>.toCurrencyDateList(): List<CurrencyWithDate> {
    return map { it.toCurrencyDate() }
}

fun List<CurrencyEntity>.toCurrencyDateEntityList(): List<CurrencyDateEntity> {
    return map { it.toCurrencyDateEntity() }
}

fun List<CurrencyEntity>.toCurrencyList(): List<Currency> {
    return map { it.toCurrency() }
}

fun List<CurrencyResponse>.toCurrencyEntityList(): List<CurrencyEntity> {
    return map { it.toCurrencyEntity() }
}

fun List<Currency>.toCurrencyDisplayItemList(): List<CurrencyDisplayItem> {
    return map { it.toCurrencyDisplayItem() }
}