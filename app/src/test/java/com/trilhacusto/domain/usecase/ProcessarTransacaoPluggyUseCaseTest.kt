package com.trilhacusto.domain.usecase

import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.domain.repository.TransacaoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProcessarTransacaoPluggyUseCaseTest {

    private val repository: TransacaoRepository = mockk(relaxed = true)
    private val useCase = ProcessarTransacaoPluggyUseCase(repository)

    @Test
    fun deve_associar_categoria_ignorando_case_do_texto() = runTest {
        // Arrange
        val regra = RegraAutomacaoEntity(
            id = 1L,
            termoBusca = "UBER", // Termo em maiúsculo
            categoriaIdPadrao = 99L
        )

        coEvery { repository.buscarRegrasAutomacao() } returns listOf(regra)
        coEvery { repository.getTransacaoById("txn_1") } returns null

        val dto = TransacaoPluggyDto(
            id = "txn_1",
            amount = 15.0,
            description = "Viagem uber 99", // Termo em minúsculo
            date = 12345L
        )

        // Act
        useCase(listOf(dto))

        // Assert
        coVerify(exactly = 1) {
            repository.salvarTransacao(
                match { it.id == "txn_1" && it.categoriaId == 99L && it.statusAtribuicao == "PENDENTE" }
            )
        }
    }

    @Test
    fun deve_salvar_com_categoria_nula_quando_nenhuma_regra_der_match() = runTest {
        // Arrange
        val regra = RegraAutomacaoEntity(
            id = 1L,
            termoBusca = "IFOOD",
            categoriaIdPadrao = 99L
        )

        coEvery { repository.buscarRegrasAutomacao() } returns listOf(regra)
        coEvery { repository.getTransacaoById("txn_2") } returns null

        val dto = TransacaoPluggyDto(
            id = "txn_2",
            amount = 45.0,
            description = "Restaurante Desconhecido", // Não dá match
            date = 12345L
        )

        // Act
        useCase(listOf(dto))

        // Assert
        coVerify(exactly = 1) {
            repository.salvarTransacao(
                match { it.id == "txn_2" && it.categoriaId == null && it.statusAtribuicao == "PENDENTE" }
            )
        }
    }

    @Test
    fun deve_atualizar_transacao_quando_estiver_como_pendencia_aberta() = runTest {
        // Arrange
        val existente = TransacaoEntity(
            id = "txn_pendente",
            valorTotal = 50.0,
            descricaoOriginal = "COMPRA ANTIGA",
            dataHora = 1000L,
            statusAtribuicao = "PENDENTE"
        )

        coEvery { repository.getTransacaoById("txn_pendente") } returns existente

        val dto = TransacaoPluggyDto(
            id = "txn_pendente",
            amount = 60.0,
            description = "COMPRA ATUALIZADA",
            date = 2000L
        )

        // Act
        useCase(listOf(dto))

        // Assert
        coVerify(exactly = 1) {
            repository.atualizarTransacao(
                match {
                    it.id == "txn_pendente" &&
                            it.valorTotal == 60.0 &&
                            it.descricaoOriginal == "COMPRA ATUALIZADA" &&
                            it.dataHora == 2000L &&
                            it.statusAtribuicao == "PENDENTE"
                }
            )
        }
    }

    @Test
    fun nao_deve_atualizar_transacao_se_ja_foi_cadastrada_como_compra_na_fatura() = runTest {
        // Arrange
        val transacaoFatura = TransacaoEntity(
            id = "txn_fatura",
            valorTotal = 150.0,
            descricaoOriginal = "SUPERMERCADO",
            dataHora = 5000L,
            statusAtribuicao = "TOTALMENTE_ATRIBUIDO"
        )

        coEvery { repository.getTransacaoById("txn_fatura") } returns transacaoFatura

        val dto = TransacaoPluggyDto(
            id = "txn_fatura",
            amount = 160.0,
            description = "SUPERMERCADO MODIFICADO",
            date = 5500L
        )

        // Act
        useCase(listOf(dto))

        // Assert
        coVerify(exactly = 0) {
            repository.atualizarTransacao(any())
        }
        coVerify(exactly = 0) {
            repository.salvarTransacao(any())
        }
    }

    @Test
    fun nao_deve_atualizar_transacao_se_estiver_com_status_ignorado() = runTest {
        // Arrange
        val transacaoExcluida = TransacaoEntity(
            id = "txn_excluida",
            valorTotal = 20.0,
            descricaoOriginal = "DESPESA IGNORADA",
            dataHora = 3000L,
            statusAtribuicao = "IGNORADO"
        )

        coEvery { repository.getTransacaoById("txn_excluida") } returns transacaoExcluida

        val dto = TransacaoPluggyDto(
            id = "txn_excluida",
            amount = 25.0,
            description = "DESPESA IGNORADA 2",
            date = 3500L
        )

        // Act
        useCase(listOf(dto))

        // Assert
        coVerify(exactly = 0) {
            repository.atualizarTransacao(any())
        }
        coVerify(exactly = 0) {
            repository.salvarTransacao(any())
        }
    }
}
