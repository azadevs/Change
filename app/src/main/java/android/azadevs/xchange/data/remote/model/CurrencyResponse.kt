package android.azadevs.xchange.data.remote.model


import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("cb_price")
    val cbPrice: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("nbu_buy_price")
    val nbuBuyPrice: String? = null,
    @SerializedName("nbu_cell_price")
    val nbuCellPrice: String? = null,
    @SerializedName("title")
    val title: String
)