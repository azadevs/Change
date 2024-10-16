package android.azadevs.xchange.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by : Azamat Kalmurzaev
 * 04/10/24
 */
@Entity(tableName = "currency")
data class CurrencyEntity(
    val price: String,
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val date: String,
    val buyPrice: String? = null,
    val sellPrice: String? = null,
    val title: String
)
