package android.azadevs.xchange.ui.model

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
data class CurrencyDisplayItem(
    val code: String,
    val title: String,
    val cbPrice: String,
    val nbuBuyPrice: String,
    val nbuCellPrice: String,
    val date: String,
    val currencyImageUri: String? = null
)
