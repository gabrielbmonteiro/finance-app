package com.trilhacusto.util

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrencyString(isPrivacyMode: Boolean = false): String {
    if (isPrivacyMode) return "R$ ••••••••"
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val formatted = format.format(kotlin.math.abs(this))
    return if (this < 0) "-$formatted" else formatted
}

fun Long.formatDateWithYearIfNeeded(includeTime: Boolean = false): String {
    val calAtual = java.util.Calendar.getInstance()
    val calData = java.util.Calendar.getInstance().apply { timeInMillis = this@formatDateWithYearIfNeeded }
    val mesmoAno = calAtual.get(java.util.Calendar.YEAR) == calData.get(java.util.Calendar.YEAR)
    
    val pattern = when {
        includeTime -> "dd MMM yy, HH:mm"
        else -> "dd MMM yy"
    }
    return java.text.SimpleDateFormat(pattern, Locale("pt", "BR")).format(java.util.Date(this)).replace(".", "")
}
