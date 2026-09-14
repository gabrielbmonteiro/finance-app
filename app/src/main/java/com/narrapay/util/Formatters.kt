package com.narrapay.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return format.format(this)
}
