package com.ekkoe.fitty.extension

import android.content.res.Resources
import android.util.TypedValue
import java.util.regex.Pattern

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

fun String?.removeBlank(): String? {
    return this?.let {
        Pattern.compile("\\s*|\t|\n|\r").matcher(it).replaceAll("")
    }
}
