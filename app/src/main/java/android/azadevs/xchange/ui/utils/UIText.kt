package android.azadevs.xchange.ui.utils

import android.content.Context
import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicText(val value: String) : UIText()

    data class ResourceText(@StringRes val value: Int) : UIText()
}

fun UIText.asString(context: Context): String {
    return when (this) {
        is UIText.DynamicText -> this.value
        is UIText.ResourceText -> context.getString(this.value)
    }
}
