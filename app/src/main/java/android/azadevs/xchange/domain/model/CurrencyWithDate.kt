package android.azadevs.xchange.domain.model

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
data class CurrencyWithDate(
    val code: String,
    val date: String,
    val buyPrice: String,
    val sellPrice: String
)
