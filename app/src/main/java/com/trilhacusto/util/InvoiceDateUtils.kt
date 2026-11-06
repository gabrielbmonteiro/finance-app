package com.trilhacusto.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class MesAno(
    val mes: Int,
    val ano: Int
) : Comparable<MesAno> {
    override fun compareTo(other: MesAno): Int {
        val anoComp = this.ano.compareTo(other.ano)
        return if (anoComp != 0) anoComp else this.mes.compareTo(other.mes)
    }
}

data class LimitesFatura(
    val inicioTimestamp: Long,
    val fimTimestamp: Long,
    val nomeMes: String,
    val periodoFormatado: String,
    val periodoExtenso: String,
    val periodoExtrato: String = ""
)

object InvoiceDateUtils {

    /**
     * Calcula as datas de início e fim da fatura para um determinado mês e ano,
     * considerando o dia de fechamento configurado.
     *
     * Por exemplo:
     * Para Outubro/2026 com fechamento no dia 25:
     * - Início: 26 de Setembro de 2026, 00:00:00.000
     * - Fim: 25 de Outubro de 2026, 23:59:59.999
     */
    fun calcularLimitesFatura(
        diaFechamento: Int,
        mesSelecionado: Int,
        anoSelecionado: Int,
        inicioTimestampCustomizado: Long? = null
    ): LimitesFatura {
        val c = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, mesSelecionado)
            set(Calendar.YEAR, anoSelecionado)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Fim da fatura: dia do fechamento do mês selecionado às 23:59:59.999
        val fim = Calendar.getInstance().apply {
            timeInMillis = c.timeInMillis
            val maxDayThisMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
            val actualClosingDay = diaFechamento.coerceIn(1, maxDayThisMonth)
            set(Calendar.DAY_OF_MONTH, actualClosingDay)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        // Início da fatura: dia seguinte ao fechamento do mês anterior às 00:00:00.000 (ou início customizado)
        val inicio = Calendar.getInstance().apply {
            if (inicioTimestampCustomizado != null) {
                timeInMillis = inicioTimestampCustomizado
            } else {
                timeInMillis = c.timeInMillis
                add(Calendar.MONTH, -1)
                val maxDayPrevMonth = getActualMaximum(Calendar.DAY_OF_MONTH)
                val actualClosingDayPrev = diaFechamento.coerceIn(1, maxDayPrevMonth)
                if (actualClosingDayPrev >= maxDayPrevMonth) {
                    // Se o fechamento era o último dia do mês anterior, o início é dia 1 deste mês
                    timeInMillis = c.timeInMillis
                    set(Calendar.DAY_OF_MONTH, 1)
                } else {
                    set(Calendar.DAY_OF_MONTH, actualClosingDayPrev + 1)
                }
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        val formatter = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))
        val nomeMes = formatter.format(c.time).replaceFirstChar { it.uppercase() }

        val sdfCurto = SimpleDateFormat("dd MMM", Locale("pt", "BR"))
        val periodoCurto = "${sdfCurto.format(inicio.time).replace(".", "").lowercase()} - ${sdfCurto.format(fim.time).replace(".", "").lowercase()}"

        val sdfLongo = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val periodoLongo = "${sdfLongo.format(inicio.time)} a ${sdfLongo.format(fim.time)}"

        val inicioAno = inicio.get(Calendar.YEAR)
        val fimAno = fim.get(Calendar.YEAR)
        val periodoExtrato = if (inicioAno != fimAno) {
            val sdfAno = SimpleDateFormat("dd MMM/yy", Locale("pt", "BR"))
            "${sdfAno.format(inicio.time).replace(".", "").lowercase()} - ${sdfAno.format(fim.time).replace(".", "").lowercase()}"
        } else {
            "${sdfCurto.format(inicio.time).replace(".", "").lowercase()} - ${sdfCurto.format(fim.time).replace(".", "").lowercase()}"
        }

        return LimitesFatura(
            inicioTimestamp = inicio.timeInMillis,
            fimTimestamp = fim.timeInMillis,
            nomeMes = nomeMes,
            periodoFormatado = periodoCurto,
            periodoExtenso = periodoLongo,
            periodoExtrato = periodoExtrato
        )
    }

    /**
     * Reconstrói os LimitesFatura a partir dos timestamps de início e fim já fixados/fechados.
     */
    fun criarLimitesAPartirDeTimestamps(
        inicioTimestamp: Long,
        fimTimestamp: Long,
        mes: Int,
        ano: Int
    ): LimitesFatura {
        val c = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, mes)
            set(Calendar.YEAR, ano)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val inicio = Calendar.getInstance().apply { timeInMillis = inicioTimestamp }
        val fim = Calendar.getInstance().apply { timeInMillis = fimTimestamp }

        val formatter = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))
        val nomeMes = formatter.format(c.time).replaceFirstChar { it.uppercase() }

        val sdfCurto = SimpleDateFormat("dd MMM", Locale("pt", "BR"))
        val periodoCurto = "${sdfCurto.format(inicio.time).replace(".", "").lowercase()} - ${sdfCurto.format(fim.time).replace(".", "").lowercase()}"

        val sdfLongo = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        val periodoLongo = "${sdfLongo.format(inicio.time)} a ${sdfLongo.format(fim.time)}"

        val inicioAno = inicio.get(Calendar.YEAR)
        val fimAno = fim.get(Calendar.YEAR)
        val periodoExtrato = if (inicioAno != fimAno) {
            val sdfAno = SimpleDateFormat("dd MMM/yy", Locale("pt", "BR"))
            "${sdfAno.format(inicio.time).replace(".", "").lowercase()} - ${sdfAno.format(fim.time).replace(".", "").lowercase()}"
        } else {
            "${sdfCurto.format(inicio.time).replace(".", "").lowercase()} - ${sdfCurto.format(fim.time).replace(".", "").lowercase()}"
        }

        return LimitesFatura(
            inicioTimestamp = inicioTimestamp,
            fimTimestamp = fimTimestamp,
            nomeMes = nomeMes,
            periodoFormatado = periodoCurto,
            periodoExtenso = periodoLongo,
            periodoExtrato = periodoExtrato
        )
    }

    /**
     * Identifica a fatura (mês e ano) a que uma transação pertence com base no seu timestamp
     * e no dia de fechamento configurado.
     *
     * Se o dia da transação for posterior ao dia de fechamento daquele mês,
     * a transação pertence à fatura do mês subsequente.
     */
    fun identificarFaturaDaTransacao(timestamp: Long, diaFechamento: Int): MesAno {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val dia = cal.get(Calendar.DAY_OF_MONTH)
        val maxDiaMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val fechamentoEfetivo = diaFechamento.coerceIn(1, maxDiaMes)

        if (dia > fechamentoEfetivo) {
            cal.add(Calendar.MONTH, 1)
        }

        return MesAno(
            mes = cal.get(Calendar.MONTH),
            ano = cal.get(Calendar.YEAR)
        )
    }
}
