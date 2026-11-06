package com.trilhacusto.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class InvoiceDateUtilsTest {

    @Test
    fun deve_calcular_limites_corretos_para_outubro_com_fechamento_25() {
        val limites = InvoiceDateUtils.calcularLimitesFatura(
            diaFechamento = 25,
            mesSelecionado = Calendar.OCTOBER, // 9
            anoSelecionado = 2026
        )

        val calInicio = Calendar.getInstance().apply { timeInMillis = limites.inicioTimestamp }
        val calFim = Calendar.getInstance().apply { timeInMillis = limites.fimTimestamp }

        // Início deve ser 26 de Setembro de 2026 00:00:00.000
        assertEquals(26, calInicio.get(Calendar.DAY_OF_MONTH))
        assertEquals(Calendar.SEPTEMBER, calInicio.get(Calendar.MONTH))
        assertEquals(2026, calInicio.get(Calendar.YEAR))
        assertEquals(0, calInicio.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, calInicio.get(Calendar.MINUTE))
        assertEquals(0, calInicio.get(Calendar.SECOND))
        assertEquals(0, calInicio.get(Calendar.MILLISECOND))

        // Fim deve ser 25 de Outubro de 2026 23:59:59.999
        assertEquals(25, calFim.get(Calendar.DAY_OF_MONTH))
        assertEquals(Calendar.OCTOBER, calFim.get(Calendar.MONTH))
        assertEquals(2026, calFim.get(Calendar.YEAR))
        assertEquals(23, calFim.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, calFim.get(Calendar.MINUTE))
        assertEquals(59, calFim.get(Calendar.SECOND))
        assertEquals(999, calFim.get(Calendar.MILLISECOND))

        assertEquals("Outubro 2026", limites.nomeMes)
        assertEquals("26 set - 25 out", limites.periodoFormatado)
    }

    @Test
    fun deve_calcular_limites_corretos_na_virada_de_ano() {
        // Janeiro de 2026 com fechamento 20
        val limites = InvoiceDateUtils.calcularLimitesFatura(
            diaFechamento = 20,
            mesSelecionado = Calendar.JANUARY, // 0
            anoSelecionado = 2026
        )

        val calInicio = Calendar.getInstance().apply { timeInMillis = limites.inicioTimestamp }
        val calFim = Calendar.getInstance().apply { timeInMillis = limites.fimTimestamp }

        // Início deve ser 21 de Dezembro de 2025
        assertEquals(21, calInicio.get(Calendar.DAY_OF_MONTH))
        assertEquals(Calendar.DECEMBER, calInicio.get(Calendar.MONTH))
        assertEquals(2025, calInicio.get(Calendar.YEAR))

        // Fim deve ser 20 de Janeiro de 2026
        assertEquals(20, calFim.get(Calendar.DAY_OF_MONTH))
        assertEquals(Calendar.JANUARY, calFim.get(Calendar.MONTH))
        assertEquals(2026, calFim.get(Calendar.YEAR))

        assertEquals("21 dez/25 - 20 jan/26", limites.periodoExtrato)
        assertEquals("21 dez - 20 jan", limites.periodoFormatado)
    }

    @Test
    fun deve_calcular_periodo_extrato_no_mesmo_ano() {
        val limites = InvoiceDateUtils.calcularLimitesFatura(
            diaFechamento = 25,
            mesSelecionado = Calendar.OCTOBER, // 9
            anoSelecionado = 2026
        )

        assertEquals("26 set - 25 out", limites.periodoExtrato)
        assertEquals("26 set - 25 out", limites.periodoFormatado)
    }

    @Test
    fun deve_formatar_mes_de_novembro_sem_ponto() {
        val limites = InvoiceDateUtils.calcularLimitesFatura(
            diaFechamento = 25,
            mesSelecionado = Calendar.NOVEMBER, // 10
            anoSelecionado = 2026
        )

        assertEquals("26 out - 25 nov", limites.periodoFormatado)
        org.junit.Assert.assertFalse(limites.periodoFormatado.contains("."))
    }

    @Test
    fun deve_criar_limites_a_partir_de_timestamps_de_fatura_fechada() {
        val limitesOriginal = InvoiceDateUtils.calcularLimitesFatura(
            diaFechamento = 25,
            mesSelecionado = Calendar.NOVEMBER,
            anoSelecionado = 2026
        )

        val limitesReconstruidos = InvoiceDateUtils.criarLimitesAPartirDeTimestamps(
            inicioTimestamp = limitesOriginal.inicioTimestamp,
            fimTimestamp = limitesOriginal.fimTimestamp,
            mes = Calendar.NOVEMBER,
            ano = 2026
        )

        assertEquals(limitesOriginal.inicioTimestamp, limitesReconstruidos.inicioTimestamp)
        assertEquals(limitesOriginal.fimTimestamp, limitesReconstruidos.fimTimestamp)
        assertEquals(limitesOriginal.nomeMes, limitesReconstruidos.nomeMes)
        assertEquals(limitesOriginal.periodoFormatado, limitesReconstruidos.periodoFormatado)
        assertEquals(limitesOriginal.periodoExtrato, limitesReconstruidos.periodoExtrato)
        org.junit.Assert.assertFalse(limitesReconstruidos.periodoFormatado.contains("."))
    }

    @Test
    fun deve_identificar_fatura_correta_da_transacao() {
        val diaFechamento = 25

        // Transação no dia 25 de Outubro às 14:00 -> fatura de Outubro 2026
        val cal1 = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 25, 14, 0, 0)
        }
        val fatura1 = InvoiceDateUtils.identificarFaturaDaTransacao(cal1.timeInMillis, diaFechamento)
        assertEquals(Calendar.OCTOBER, fatura1.mes)
        assertEquals(2026, fatura1.ano)

        // Transação no dia 26 de Setembro às 08:00 -> fatura de Outubro 2026 (após fechamento de Setembro)
        val cal2 = Calendar.getInstance().apply {
            set(2026, Calendar.SEPTEMBER, 26, 8, 0, 0)
        }
        val fatura2 = InvoiceDateUtils.identificarFaturaDaTransacao(cal2.timeInMillis, diaFechamento)
        assertEquals(Calendar.OCTOBER, fatura2.mes)
        assertEquals(2026, fatura2.ano)

        // Transação no dia 26 de Outubro às 10:00 -> fatura de Novembro 2026 (após fechamento de Outubro)
        val cal3 = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 26, 10, 0, 0)
        }
        val fatura3 = InvoiceDateUtils.identificarFaturaDaTransacao(cal3.timeInMillis, diaFechamento)
        assertEquals(Calendar.NOVEMBER, fatura3.mes)
        assertEquals(2026, fatura3.ano)

        // Transação no dia 28 de Dezembro de 2026 -> fatura de Janeiro de 2027 (virada de ano)
        val cal4 = Calendar.getInstance().apply {
            set(2026, Calendar.DECEMBER, 28, 12, 0, 0)
        }
        val fatura4 = InvoiceDateUtils.identificarFaturaDaTransacao(cal4.timeInMillis, diaFechamento)
        assertEquals(Calendar.JANUARY, fatura4.mes)
        assertEquals(2027, fatura4.ano)
    }
}
