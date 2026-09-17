package com.trilhacusto.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class NotificationParserTest {

    @Test
    fun deve_extrair_valor_decimal_com_virgula() {
        val texto = "Compra aprovada no cartão final 1234 - R$ 45,90 em RESTAURANTE ABC às 12:30."
        val result = NotificationParser.parse(texto)
        
        assertNotNull(result)
        assertEquals(45.90, result?.valor!!, 0.0)
        assertEquals("RESTAURANTE ABC", result.estabelecimento)
    }

    @Test
    fun deve_extrair_valor_milhar_com_ponto() {
        val texto = "Aviso: R$ 1.500,00 em LOJA DE MOVEIS às 18:00 no seu cartão."
        val result = NotificationParser.parse(texto)
        
        assertNotNull(result)
        assertEquals(1500.00, result?.valor!!, 0.0)
        assertEquals("LOJA DE MOVEIS", result.estabelecimento)
    }

    @Test
    fun deve_retornar_nulo_para_texto_nao_financeiro() {
        val texto = "Seu código do iFood é 1234. Não compartilhe com ninguém."
        val result = NotificationParser.parse(texto)
        
        assertNull("Deveria retornar nulo para notificação não financeira", result)
    }

    @Test
    fun deve_extrair_nome_do_estabelecimento() {
        val texto = "Pagamento de R$ 9,99 em ASSINATURA NETFLIX às 01:00 aprovado."
        val result = NotificationParser.parse(texto)
        
        assertNotNull(result)
        assertEquals("ASSINATURA NETFLIX", result?.estabelecimento)
        assertEquals(9.99, result?.valor!!, 0.0)
    }
}
