package android.azadevs.xchange.core.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Created by : Azamat Kalmurzaev
 * 09/10/24
 */
object XchangeUtilities {

    fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED) &&
                    (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) -> true

            else -> false
        }
    }

    fun formatterCurrencyPrice(price: String) = if (price.isNotEmpty()) "${
        String.format(
            Locale.getDefault(),
            "%,d",
            price.toDouble().toLong()
        ).replace(",", " ")
    } UZS" else "N/A"

    fun getDateFormat(
        date: String,
        inputFormat: String = "dd.MM.yyyy HH:mm:ss",
        outputFormat: String = "dd.MM.yyyy"
    ): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val parsedDate = inputFormatter.parse(date) ?: return ""
        return outputFormatter.format(parsedDate)
    }

    fun getTimeFormat(
        date: String,
        inputFormat: String = "dd.MM.yyyy HH:mm:ss",
        outputFormat: String = "HH:mm"
    ): String {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val parsedDate = inputFormatter.parse(date) ?: return ""
        return outputFormatter.format(parsedDate)
    }

    fun getImageUrl(code: String) = "${Constants.IMAGE_URL + code}.png"

}