package com.narrapay.util

object NotificationParser {


    private val regex = Regex("""R\$\s*([\d.]+[.,]\d{2})\s+em\s+(.*?)\s+às""")

    data class ParsedData(val valor: Double, val estabelecimento: String)

    fun parse(texto: String): ParsedData? {
        val matchResult = regex.find(texto)
        if (matchResult != null) {

            val rawValor = matchResult.groupValues[1]


            val valorString = rawValor.replace(".", "").replace(",", ".")
            
            val valor = valorString.toDoubleOrNull()
            
            val estabelecimento = matchResult.groupValues[2].trim()

            if (valor != null) {
                return ParsedData(valor, estabelecimento)
            }
        }
        return null
    }
}
