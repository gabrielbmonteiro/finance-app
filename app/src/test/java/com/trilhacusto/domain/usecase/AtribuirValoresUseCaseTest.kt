package com.trilhacusto.domain.usecase

import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.domain.exception.AtribuicaoDivergenteException
import com.trilhacusto.domain.repository.TransacaoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AtribuirValoresUseCaseTest {

    // Mock do repositório
    private val repository: TransacaoRepository = mockk()
    
    // Instância do UseCase injetando o mock
    private val useCase = AtribuirValoresUseCase(repository)

    @Test
    fun deve_salvar_rateio_quando_soma_for_exata() = runTest {
        // Arrange
        val transacaoEntity = TransacaoEntity(
            id = "txn_123",
            valorTotal = 150.0,
            descricaoOriginal = "Compra de Teste",
            dataHora = 123456789L,
            statusAtribuicao = "PENDENTE"
        )
        
        val transacaoCompleta = TransacaoCompleta(
            transacao = transacaoEntity,
            categoria = null,
            atribuicoes = emptyList()
        )
        
        val novasAtribuicoes = mapOf(
            1L to 100.0,
            2L to 50.0
        )

        coEvery { repository.atribuirRateio(any(), any(), any()) } returns Unit

        // Act
        useCase(transacaoCompleta, novasAtribuicoes)

        // Assert
        coVerify(exactly = 1) {
            repository.atribuirRateio(
                transacaoId = "txn_123",
                transacaoAtualizada = match { it.statusAtribuicao == "TOTALMENTE_ATRIBUIDO" },
                novasAtribuicoes = match { 
                    it.size == 2 && 
                    it.any { atr -> atr.pessoaId == 1L && atr.valorAtribuido == 100.0 } &&
                    it.any { atr -> atr.pessoaId == 2L && atr.valorAtribuido == 50.0 }
                }
            )
        }
    }

    @Test
    fun deve_lancar_excecao_quando_soma_for_divergente() = runTest {
        // Arrange
        val transacaoEntity = TransacaoEntity(
            id = "txn_123",
            valorTotal = 150.0,
            descricaoOriginal = "Compra de Teste",
            dataHora = 123456789L,
            statusAtribuicao = "PENDENTE"
        )
        
        val transacaoCompleta = TransacaoCompleta(
            transacao = transacaoEntity,
            categoria = null,
            atribuicoes = emptyList()
        )
        
        val novasAtribuicoesIncorretas = mapOf(
            1L to 100.0,
            2L to 40.0 // Soma = 140.0, divergente do valorTotal 150.0
        )

        // Act
        val result = runCatching {
            useCase(transacaoCompleta, novasAtribuicoesIncorretas)
        }

        // Assert
        val exception = result.exceptionOrNull()
        assertTrue("Deveria lançar AtribuicaoDivergenteException", exception is AtribuicaoDivergenteException)
        
        coVerify(exactly = 0) {
            repository.atribuirRateio(any(), any(), any())
        }
    }

    @Test
    fun deve_passar_mesmo_com_imprecisao_de_ponto_flutuante() = runTest {
        // Arrange
        val transacaoEntity = TransacaoEntity(
            id = "txn_123",
            valorTotal = 150.0,
            descricaoOriginal = "Compra de Teste",
            dataHora = 123456789L,
            statusAtribuicao = "PENDENTE"
        )
        val transacaoCompleta = TransacaoCompleta(transacaoEntity, null, emptyList())
        
        // Simulação matemática bruta de ponto flutuante que daria imprecisão: 100.0 + 49.9999998
        // Para Double, o compilador já avalia na criação, vamos forçar a situação passando o Double
        val novasAtribuicoes = mapOf(
            1L to 100.0,
            2L to 49.9999999 // Será arredondado para 50.00 ao longo do BigDecimal setScale
        )

        coEvery { repository.atribuirRateio(any(), any(), any()) } returns Unit

        // Act
        useCase(transacaoCompleta, novasAtribuicoes)

        // Assert
        coVerify(exactly = 1) { repository.atribuirRateio(any(), any(), any()) }
    }

    @Test
    fun deve_falhar_se_houver_valor_negativo_no_rateio() = runTest {
        // Arrange
        val transacaoEntity = TransacaoEntity(
            id = "txn_123",
            valorTotal = 150.0,
            descricaoOriginal = "Compra",
            dataHora = 123L,
            statusAtribuicao = "PENDENTE"
        )
        val transacaoCompleta = TransacaoCompleta(transacaoEntity, null, emptyList())
        
        val novasAtribuicoes = mapOf(
            1L to 200.0,
            2L to -50.0 // Soma até dá 150.0, mas tem valor negativo
        )

        // Act
        val result = runCatching {
            useCase(transacaoCompleta, novasAtribuicoes)
        }

        // Assert
        val exception = result.exceptionOrNull()
        assertTrue("Deveria lançar IllegalArgumentException", exception is IllegalArgumentException)
        
        coVerify(exactly = 0) { repository.atribuirRateio(any(), any(), any()) }
    }

    @Test
    fun deve_lancar_excecao_quando_transacao_for_nula() = runTest {
        // Act
        val result = runCatching {
            useCase(null, mapOf(1L to 50.0))
        }

        // Assert
        val exception = result.exceptionOrNull()
        assertTrue("Deveria lançar IllegalArgumentException", exception is IllegalArgumentException)
        coVerify(exactly = 0) { repository.atribuirRateio(any(), any(), any()) }
    }

    @Test
    fun deve_lancar_excecao_quando_transacao_id_for_vazio() = runTest {
        // Arrange
        val transacaoEntity = TransacaoEntity(
            id = "   ", // ID vazio
            valorTotal = 150.0,
            descricaoOriginal = "Compra",
            dataHora = 123L,
            statusAtribuicao = "PENDENTE"
        )
        val transacaoCompleta = TransacaoCompleta(transacaoEntity, null, emptyList())

        // Act
        val result = runCatching {
            useCase(transacaoCompleta, mapOf(1L to 150.0))
        }

        // Assert
        val exception = result.exceptionOrNull()
        assertTrue("Deveria lançar IllegalArgumentException para ID vazio", exception is IllegalArgumentException)
        coVerify(exactly = 0) { repository.atribuirRateio(any(), any(), any()) }
    }
}
