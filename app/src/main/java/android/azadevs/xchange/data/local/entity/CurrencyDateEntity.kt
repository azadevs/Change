package android.azadevs.xchange.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
@Entity("currency_date")
data class CurrencyDateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val nbBuyPrice: String,
    val nbSellPrice: String,
    val code: String
)
