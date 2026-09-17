package com.trilhacusto.domain.usecase

import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
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
                match { it.id == "txn_1" && it.categoriaId == 99L }
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
                match { it.id == "txn_2" && it.categoriaId == null }
            ) 
        }
    }
}
