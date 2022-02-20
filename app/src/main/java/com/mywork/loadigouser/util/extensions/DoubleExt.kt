@file:JvmName("DoubleExt")
package com.mywork.loadigouser.util.extensions

import java.text.DecimalFormat

@JvmOverloads
fun Double.toFormattedString(format: String = "0.##"): String {
    return DecimalFormat(format).apply {
        this.minimumFractionDigits = 2
    }.format(this)
}