package android.azadevs.xchange.domain.model

/**
 * Created by : Azamat Kalmurzaev
 * 03/10/24
 */
data class Currency(
    val code: String,
    val title: String,
    val cbPrice: String,
    val nbuBuyPrice: String,
    val nbuCellPrice: String,
    val date: String,
    val currencyImageUri: String? = null
)
